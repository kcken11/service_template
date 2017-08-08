package com.melot.talkee.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kktalkee.crm.module.domain.PrivilegeInfo;
import com.kktalkee.crm.module.domain.ResultObject;
import com.kktalkee.crm.module.service.TlkAdminService;
import com.melot.kktv.util.DateUtil;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.dao.*;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.service.*;
import com.melot.talkee.redis.LessonRedisSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;

@Service
public class TalkOrderServiceImpl implements TalkOrderService {

    private static final Logger LOGGER = Logger.getLogger(TalkOrderServiceImpl.class);

    private static Logger hadoopLogger = Logger.getLogger("hadoopLogger");

    private static Gson GSON = new Gson();

    @Autowired
    private TalkUserService talkUserService;

    @Autowired
    private TalkLessonService talkLessonService;

    @Autowired
    private TalkPublishService talkPublishService;

    @Autowired
    private PublishLessonMapper publishLessonMapper;

    @Autowired
    private StudentLessonMapper studentLessonMapper;

    @Autowired
    private TurnStudentLessonMapper turnStudentLessonMapper;

    @Autowired
    private TeacherDetailCommentMapper teacherDetailCommentMapper;

    @Autowired
    private StudentCheckinMapper studentCheckinMapper;

    @Autowired
    private TeacherCheckinMapper teacherCheckinMapper;

    @Autowired
    private ActionLessioningMapper actionLessioningMapper;

    @Autowired
    private StudentPeriodsMapper studentPeriodsMapper;

    @Autowired
    private ApplyPeriodsMapper applyPeriodsMapper;

    @Autowired
    private TeacherPerformanceMapper teacherLessonHistMapper;

    @Autowired
    private TalkEmailService talkEmailService;

    @Autowired
    private UserStudentMapper userStudentMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    @Transactional
    public String orderLesson(Integer userId, List<Integer> periodList, Integer orderUser) {
        int orderCount = 0;
        int adviceperiodid = 0;
        String tagCode = TalkTagCodeEnum.SUCCESS;
        if (userId != null && periodList != null && periodList.size() > 0 && orderUser != null) {
            hadoopLogger.info("userId:"+userId+", periodList:"+GSON.toJson(periodList)+", orderUser:"+orderUser);
            StudentInfo studentInfo = talkUserService.getStudentInfoByUserId(userId);
            if (studentInfo != null) {

                hadoopLogger.info("userId:"+userId+", studentInfo:"+GSON.toJson(studentInfo));
                Integer userSubLevel = studentInfo.getSubLevel();
                Integer userLevel = studentInfo.getUserLevel();
                int userType = studentInfo.getUserType();
                if (userLevel == null || userLevel.intValue() == 0) {
                    LOGGER.info("TalkOrderService.orderLesson userId:" + userId + ",userLevel:" + userLevel + ",userType:" + userType + " unauthorized");
                    return TalkTagCodeEnum.UNAUTHORIZED;
                }
                if (userSubLevel == null) {
                    userSubLevel = 0;
                }
                if (userId.intValue() != orderUser.intValue()) {
                    boolean auth = false;
                    // 校验是否有约课权限
                    TlkAdminService tlkAdminService = MelotBeanFactory.getBean("tlkAdminService", TlkAdminService.class);
                    ResultObject resultObject = tlkAdminService.getHavePrivilegeByActionName(orderUser, "orderLesson");
                    if(resultObject.isSuccess()&&resultObject.getData()!=null){
                        List<PrivilegeInfo> list= (List<PrivilegeInfo>) resultObject.getData();
                        if (list.size() > 0) {
                            auth = true;
                        }
                    }

                    if (userType < 1) {
                        // userId和orderUser 不相同，并且cancleUser不等于对应CR
                        if (studentInfo.getCcId() == null) {
                            LOGGER.info("TalkOrderService.orderLesson userId:" + userId + ",ccId:" + studentInfo.getCcId() + ",userType:" + userType + " unauthorized");
                            return TalkTagCodeEnum.UNDISTRIBUTE_CR;
                        } else if (studentInfo.getCcId().intValue() != orderUser.intValue() && !auth) {
                            LOGGER.info("TalkOrderService.orderLesson userId:" + userId + ",ccId:" + studentInfo.getCcId() + ",userType:" + userType + ",orderUser:" + orderUser
                                    .intValue() + ",auth:" + auth + " unauthorized");
                            return TalkTagCodeEnum.UNAUTHORIZED;
                        }
                    } else {
                        // userId和orderUser 不相同，并且cancleUser不等于对应CR
                        if (studentInfo.getCrId() == null) {
                            LOGGER.info("TalkOrderService.orderLesson userId:" + userId + ",crId:" + studentInfo.getCrId() + ",userType:" + userType + " unauthorized");
                            return TalkTagCodeEnum.UNDISTRIBUTE_CR;
                        } else if (studentInfo.getCrId().intValue() != orderUser.intValue() && !auth) {
                            LOGGER.info("TalkOrderService.orderLesson userId:" + userId + ",crId:" + studentInfo.getCrId() + ",userType:" + userType + ",orderUser:" + orderUser
                                    .intValue() + ",auth:" + auth + " unauthorized");
                            return TalkTagCodeEnum.UNAUTHORIZED;
                        }
                    }
                }
                // 获取预约课程信息
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("periodIdList", periodList);
                param.put("state", TalkCommonEnum.PUBLISH_LESSON_STATE_FREE);
                List<PublishLesson> publishLessonList = publishLessonMapper.selectBySelectiveMap(param);
                if (publishLessonList != null && publishLessonList.size() > 0) {
                    // 先对预约课程分组
                    Map<Integer, List<PublishLesson>> publishTypeMap = new HashMap<Integer, List<PublishLesson>>();
                    List<PublishLesson> publishTypeList = null;
                    Date currentTime = new Date();
                    Map<String, Object> paramMap = null;
                    for (PublishLesson publishLesson : publishLessonList) {
                        // 防止重复预约加锁
                        if (LessonRedisSource.getStudentOrderTag(userId,publishLesson.getPeriodId()) > 1){
                            continue;
                        }
                        Date beginTime = publishLesson.getBeginTime();
                        Integer publishType = publishLesson.getPublishType();
                        int publishLessonState = publishLesson.getState();
                        // 开始时间小于当前时间的,不允许预约
                        if (beginTime.getTime() < currentTime.getTime()) {
                            LOGGER.info("TalkOrderService.orderLesson userId:"+userId+", periodId:"+publishLesson.getPeriodId()+", orderUser:"+orderUser+", beginTime:"+DateUtil.formatDate(beginTime, "yyyy-MM-dd HH:mm:ss")+" out order time");
                            return TalkTagCodeEnum.OUT_ORDER_TIME;
                        }
                        // 普通课
                        if (publishType == TalkCommonEnum.PUBLISH_TYPE_GENNEL) {
                            // 普通课已预约,不能重复预约
                            if (publishLessonState == TalkCommonEnum.PUBLISH_LESSON_STATE_ORDER) {
                                LOGGER.info("TalkOrderService.orderLesson userId:"+userId+", periodId:"+publishLesson.getPeriodId()+", orderUser:"+orderUser+", publishState:"+publishLessonState+" can not order");
                                return TalkTagCodeEnum.CAN_NOT_ORDER;
                            }
                            // 试听用户
                            if (userType < 1) {
                                // 试听课不允许预约五天后的课程
                                if (beginTime.getTime() - currentTime.getTime() > 1000 * 3600 * 24 * 5) {
                                    LOGGER.info("TalkOrderService.orderLesson userId:"+userId+", periodId:"+publishLesson.getPeriodId()+", orderUser:"+orderUser+", userType:"+userType+" max 5 day ,can not order");
                                    return TalkTagCodeEnum.OUT_ORDER_TIME;
                                }
                            }
                        } else if (publishType == TalkCommonEnum.PUBLISH_TYPE_PUBLIC) {
                            ;
                        } else {
                            // 调试课已预约,不能重复预约
                            if (publishLessonState == TalkCommonEnum.PUBLISH_LESSON_STATE_ORDER) {
                                LOGGER.info("TalkOrderService.orderLesson userId:"+userId+", periodId:"+publishLesson.getPeriodId()+", orderUser:"+orderUser+", publishState:"+publishLessonState+" can not order");
                                return TalkTagCodeEnum.CAN_NOT_ORDER;
                            }
                        }

                        paramMap = new HashMap<String, Object>();
                        paramMap.put("studentId", userId);
                        paramMap.put("beginTime", publishLesson.getBeginTime());
                        paramMap.put("endTime", publishLesson.getEndTime());
                        paramMap.put("lessonState", 3);
                        List<OrderLesson> orderLessonList = studentLessonMapper.isIntersect(paramMap);
                        // 检查是否有交集
                        if (orderLessonList != null && orderLessonList.size() > 0) {
                            continue;
                        }

                        if (publishTypeMap.containsKey(publishType)) {
                            publishTypeList = publishTypeMap.get(publishType);
                        } else {
                            publishTypeList = new ArrayList<PublishLesson>();
                        }
                        publishTypeList.add(publishLesson);
                        publishTypeMap.put(publishType, publishTypeList);
                    }

                    if (publishTypeMap != null && !publishTypeMap.isEmpty()) {
                        for (Map.Entry<Integer, List<PublishLesson>> entry : publishTypeMap.entrySet()) {
                            Integer publishType = entry.getKey();
                            List<PublishLesson> list = entry.getValue();
                            hadoopLogger.info("userId:"+userId+", publishType:"+publishType+", orderList:"+GSON.toJson(list));
                            tagCode = orderPublishLesson(publishType,studentInfo,list,orderUser);
                            if (!tagCode.equals(TalkTagCodeEnum.SUCCESS)) {
                                break;
                            }else{
                                orderCount = list.size();
                            }
                        }
                    } else {
                        LOGGER.info("TalkOrderService.orderLesson  periodList:[" + new Gson().toJson(periodList) + "] can not order");
                        tagCode = TalkTagCodeEnum.CAN_NOT_ORDER;
                    }
                } else {
                    LOGGER.info("TalkOrderService.orderLesson  periodList:[" + new Gson().toJson(periodList) + "] can not order");
                    tagCode = TalkTagCodeEnum.CAN_NOT_ORDER;
                }
            } else {
                LOGGER.info("TalkOrderService.orderLesson userId:[" + userId + "] query table user_student is null");
                tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
            }
        }
        // 预约成功一堂课
        if (orderCount == 1) {
            LessonRedisSource.addEmailAdvice(String.valueOf(adviceperiodid) + "-" + userId, System.currentTimeMillis() + 5 * 60 * 1000);
        } else if (orderCount > 1) {
            LessonRedisSource.addEmailAdvice(orderCount + "," + adviceperiodid + "-" + userId, System.currentTimeMillis() + 5 * 60 * 1000);
        }
        return tagCode;
    }

    private void addTryLesson(List<PublishLesson> publishLessonList, int studentId, int orderUser) {
        if (publishLessonList != null && !publishLessonList.isEmpty()) {
            for (PublishLesson publishLesson : publishLessonList) {
                Integer lessonId = publishLesson.getLessonId();
                if (lessonId != null) {
                    Lesson lesson = talkLessonService.getLessonById(lessonId);
                    if (lesson != null) {
                        TurnStudentLesson turnStudentLesson = new TurnStudentLesson();
                        turnStudentLesson.setBeginTime(publishLesson.getBeginTime());
                        turnStudentLesson.setEndTime(publishLesson.getEndTime());
                        turnStudentLesson.setLessonId(lesson.getLessonId());
                        turnStudentLesson.setTrunVideoState(0);
                        turnStudentLesson.setStudentId(studentId);
                        turnStudentLesson.setLessonUrl(lesson.getLessonUrl());
                        turnStudentLesson.setPeriodId(publishLesson.getPeriodId());
                        turnStudentLesson.setTeacherId(publishLesson.getTeacherId());
                        turnStudentLesson.setOrderUser(orderUser);
                        turnStudentLesson.setOrderTime(new Date());
                        int resultCode = turnStudentLessonMapper.insertSelective(turnStudentLesson);

                        if (resultCode > 0) {
                            publishLesson.setStudentId(studentId);
                            publishLesson.setState(2);
                            publishLessonMapper.updateByPrimaryKeySelective(publishLesson);

                            // TODO 是否允许预约当天课程
                            String currentDate = DateUtil.formatDate(new Date(), "yyyyMMdd");
                            String begintDate = DateUtil.formatDate(publishLesson.getBeginTime(), "yyyyMMdd");
                            if (currentDate.equals(begintDate)) {
                                try {
                                    LessonRedisSource.addDailyTeacherSet(publishLesson.getTeacherId());
                                    LessonRedisSource.addTeacherDailyLessonSet(publishLesson.getTeacherId(), publishLesson.getPeriodId(), publishLesson.getBeginTime());
                                } catch (Exception e) {}
                            }
                        }
                    }
                }
            }
        }
    }


    private String addPublicLesson(List<PublishLesson> publishLessonList, int studentId, int orderUser) {
        String tagCode = TalkTagCodeEnum.SUCCESS;
        if (publishLessonList != null && !publishLessonList.isEmpty()) {
            for (PublishLesson publishLesson : publishLessonList) {
                Integer lessonId = publishLesson.getLessonId();
                if (lessonId != null) {
                    Lesson lesson = talkLessonService.getLessonById(lessonId);
                    if (lesson != null) {
                        int resultCode = 0;
                        int maxNum = publishLesson.getMaxNum();
                        int orderNum = publishLesson.getOrderNum();
                        // 检测是否超过预约个数
                        if (maxNum > orderNum) {
                            StudentLesson studentLesson = new StudentLesson();
                            studentLesson.setBeginTime(publishLesson.getBeginTime());
                            studentLesson.setEndTime(publishLesson.getEndTime());
                            studentLesson.setLessonId(lesson.getLessonId());
                            studentLesson.setLessonState(0);
                            studentLesson.setStudentId(studentId);
                            studentLesson.setLessonUrl(lesson.getLessonUrl());
                            studentLesson.setPeriodId(publishLesson.getPeriodId());
                            studentLesson.setTeacherId(publishLesson.getTeacherId());
                            studentLesson.setOrderUser(orderUser);
                            studentLesson.setOrderTime(new Date());
                            studentLesson.setConsType(TalkCommonEnum.PERIOD_TYPE_PUBLIC);
                            studentLesson.setIsDetailComment(0);
                            resultCode = studentLessonMapper.insertSelective(studentLesson);

                            if (resultCode > 0) {
                                publishLesson.setOrderNum(orderNum + 1);
                                publishLesson.setStudentId(studentId);
                                publishLesson.setState(2);
                                resultCode = publishLessonMapper.updateByPrimaryKeySelective(publishLesson);
                                if (resultCode > 0) {
                                    // 修改用户公开课剩余可预约课时数
                                    StudentPeriods studentPeriods = talkUserService.getStudentPeriods(studentId, TalkCommonEnum.PERIOD_TYPE_PUBLIC);
                                    if (studentPeriods != null) {
                                        int vaildPreviwPeriods = studentPeriods.getVaildPreviwPeriods();
                                        vaildPreviwPeriods = Math.max(vaildPreviwPeriods - 1, 0);
                                        studentPeriods.setVaildPreviwPeriods(vaildPreviwPeriods);
                                        talkUserService.modifyStudentPeriods(studentPeriods);
                                    }
                                }
                                // TODO 是否允许预约当天课程
                                String currentDate = DateUtil.formatDate(new Date(), "yyyyMMdd");
                                String begintDate = DateUtil.formatDate(publishLesson.getBeginTime(), "yyyyMMdd");
                                if (currentDate.equals(begintDate)) {
                                    try {
                                        LessonRedisSource.addDailyTeacherSet(publishLesson.getTeacherId());
                                        LessonRedisSource.addTeacherDailyLessonSet(publishLesson.getTeacherId(), publishLesson.getPeriodId(), publishLesson.getBeginTime());
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return tagCode;
    }

    private String orderPublishLesson(Integer publishType, StudentInfo studentInfo, List<PublishLesson> publishLessonList, Integer orderUser) {

        String tagCode = TalkTagCodeEnum.SUCCESS;
        int userType = studentInfo.getUserType();
        int userLevel = studentInfo.getUserLevel();
        int userSubLevel = studentInfo.getSubLevel();
        int userId = studentInfo.getUserId();

        // 开始时间小于当前时间不预约
        Integer periodType = null;
        int consType = publishType;
        int overplusCount = -1;
        // 普通课、公开课
        if (publishType == TalkCommonEnum.PUBLISH_TYPE_GENNEL || publishType == TalkCommonEnum.PUBLISH_TYPE_PUBLIC) {
            // 存放已预约课程
            Map<Long, StudentLesson> orderLessonMap = new HashMap<Long, StudentLesson>();
            // 公开课没有等级，不用重新排序
            if (publishType == TalkCommonEnum.PUBLISH_TYPE_PUBLIC) {
                periodType = 2;
            } else {
                // 试听用户
                if (userType < 1) {
                    periodType = 3;
                    consType = TalkCommonEnum.PERIOD_TYPE_DEMO;
                } else {
                    periodType = 1;
                }
            }
            // 计算用户是否预约满（是否超过学时）
            // 普通课、试听课
            StudentPeriods studentPeriods = talkUserService.getStudentPeriods(userId, periodType);
            if (studentPeriods != null) {
                if (studentPeriods.getVaildPreviwPeriods() != null) {
                    overplusCount = studentPeriods.getVaildPreviwPeriods();
                    if (overplusCount > 0) {
                        overplusCount = overplusCount - publishLessonList.size();
                    }
                }
            }
            // 日志记录当前用户剩余课时、剩余可约课时、预约课时
            hadoopLogger.info("userId:" + userId + ",userType:[" + userType + "],userLevel:[" + userLevel + "],userSubLevel:[" + userSubLevel + "],periodType:["+periodType+"],curPeriods:[" + studentPeriods.getCurPeriods() + "],vaildPreviwPeriods:[" + studentPeriods.getVaildPreviwPeriods() + "],orderCount:["+publishLessonList.size()+"],overplusCount:["+overplusCount+"]");

            if (overplusCount < 0) {
                LOGGER.info("TalkOrderService.orderLesson userId:[" + userId + "],userType:[" + userType + "],overplusCount:[" + overplusCount + "] is over");
                return TalkTagCodeEnum.LEVEL_RELA_LESSON_ORDER_OVER;
            }

            int lastSubLevel = userSubLevel;
            // 获取最后预约课程ID
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("studentId", userId);
            paramMap.put("stateList", Arrays.asList(0));
            paramMap.put("consType", consType);
            StudentLesson studentLession = studentLessonMapper.getLastOrderLesson(paramMap);
            if (studentLession != null) {
                Lesson lesson = talkLessonService.getLessonById(studentLession.getLessonId());
                if (lesson != null && lesson.getLessonLevel() == userLevel) {
                    lastSubLevel = lesson.getSubLevel();
                }
            }
            // 日志记录当前用户最后预约课程 等级
            try {
                hadoopLogger.info("userId:" + userId + ",userType:[" + userType + "],consType:["+consType+"],lastOrderSubLevel:[" + lastSubLevel + "]");
            } catch (Exception e) {
            }

            if (publishType == TalkCommonEnum.PERIOD_TYPE_GENNEL) {

                Map<String, Object> tempMap = new HashMap<String, Object>();
                tempMap.put("lessonLevel", userLevel);
                tempMap.put("subLevel", lastSubLevel);
                tempMap.put("lessonType", 1);
                tempMap.put("status", 1);
                int lessonOverplusCount =  lessonMapper.selectOverplusCount(tempMap);
                if (lessonOverplusCount < publishLessonList.size()) {
                    LOGGER.info("TalkOrderService.orderLesson  userId:[" + userId + "],userType:[" + userType + "],lessonLevel:[" + userLevel + "],lastSubLevel:[" + lastSubLevel + "],lessonOverplusCount:[" + lessonOverplusCount + "],orderCount:["+publishLessonList.size()+"] out of max level ");
                    return TalkTagCodeEnum.OUT_OF_MAX_LEVEL;
                }
                // 课程信息列表转成Map存储Map<lessonLevel,Lesson>
                Map<Integer, Lesson> lessonMap = new HashMap<Integer, Lesson>();
                // 查询当前用户等级对应所有课程子等级
                List<LessonLevel> levelList = talkLessonService.selectByParentLevel(userLevel);
                // 通过userLevel获取对应子等级列表
                if (levelList != null && levelList.size() > 0) {
                    // 通过主键ID进行排序
                    Collections.sort(levelList, new Comparator<LessonLevel>() {
                        @Override
                        public int compare(LessonLevel a, LessonLevel b) {
                            Integer one = a.getLevelId();
                            Integer two = b.getLevelId();
                            return one.compareTo(two);
                        }
                    });
                } else {
                    LOGGER.info("TalkOrderService.orderLesson  userId:[" + userId + "],userType:[" + userType + "],getLessonLevelByLevel parentlevel:[" + userLevel + "] is null");
                    return TalkTagCodeEnum.LESSON_LEVEL_NOT_EXIST;
                }


                // 查询对应学生大等级的课程信息列表
                List<Lesson> lessonList = talkLessonService.getLessonList(userLevel, publishType, 1, null, null);
                if (lessonList != null && lessonList.size() > 0) {
                    for (Lesson tempLesson : lessonList) {
                        lessonMap.put(tempLesson.getSubLevel(), tempLesson);
                    }
                } else {
                    LOGGER.info("TalkOrderService.orderLesson  getLessonList userLevel:[" + userLevel + "],publishType:[" + publishType + "] query table conf_lesson is null");
                    return TalkTagCodeEnum.LEVEL_RELA_LESSON_NOT_EXIST;
                }
                // 计算已预约未结束的课程
                Map<String, Object> orderParamMap = new HashMap<String, Object>();
                orderParamMap.put("studentId", userId);
                orderParamMap.put("stateList", Arrays.asList(0, 1));
                orderParamMap.put("consType", consType);
                List<StudentLesson> orderList = studentLessonMapper.selectByUserIdAndStateList(orderParamMap);
                if (orderList != null && orderList.size() > 0) {
                    for (StudentLesson studentLesson : orderList) {
                        if (studentLesson.getLessonId() == null) {
                            // 垃圾数据
                            continue;
                        }
                        orderLessonMap.put(studentLesson.getBeginTime().getTime(), studentLesson);
                    }
                }
                for (PublishLesson publishLesson : publishLessonList) {
                    long beginTime = publishLesson.getBeginTime().getTime();
                    StudentLesson studentLesson = new StudentLesson();
                    studentLesson.setBeginTime(publishLesson.getBeginTime());
                    studentLesson.setEndTime(publishLesson.getEndTime());
                    studentLesson.setLessonState(0);
                    studentLesson.setStudentId(userId);
                    studentLesson.setPeriodId(publishLesson.getPeriodId());
                    studentLesson.setTeacherId(publishLesson.getTeacherId());
                    studentLesson.setOrderUser(orderUser);
                    studentLesson.setOrderTime(new Date());
                    studentLesson.setConsType(consType);
                    studentLesson.setIsDetailComment(0);
                    orderLessonMap.put(beginTime, studentLesson);
                }

                List<Map.Entry<Long, StudentLesson>> tempOrderList = new ArrayList<Map.Entry<Long, StudentLesson>>(orderLessonMap.entrySet());
                // 通过开始时间进行排序
                Collections.sort(tempOrderList, new Comparator<Map.Entry<Long, StudentLesson>>() {

                    @Override
                    public int compare(Entry<Long, StudentLesson> o1, Entry<Long, StudentLesson> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                });
                Lesson lesson = null;
                for (Map.Entry<Long, StudentLesson> orderEntry : tempOrderList) {
                    StudentLesson studentLesson = orderEntry.getValue();
                    // 获取学生下一个课程ID
                    // 普通课只能预约和用户等级相同的大等级
                    lesson = getNextOrderLesson(userLevel, userSubLevel, userId, studentLesson.getBeginTime(), consType, levelList, lessonMap);
                    if (lesson != null) {
                        tagCode = lesson.getTagCode();
                        if (tagCode.equals(TalkTagCodeEnum.SUCCESS)) {
                            if (lesson.getLessonId() == null) {
                                LOGGER.info("TalkOrderService.orderLesson  getNextOrderLesson is null userId:[" + userId + "],userType:[" + userType + "],userLevel:[" + userLevel + "],userSubLevel:[" + userSubLevel + "],beginTime:[" + studentLesson
                                        .getBeginTime() + "],publishType:[" + publishType + "],consType:[" + consType + "]");
                                continue;
                            }
                            // 此处检测当前预约课程开始时间是否大于已预约课程的开始时间，如果有这把已预约课程ID，往后加一
                            if (studentLesson.getLessonId() != null) {
                                if (lesson.getLessonId() != null && lesson.getLessonId().intValue() == studentLesson.getLessonId().intValue()) {
                                    continue;
                                } else {
                                    studentLesson.setLessonId(lesson.getLessonId());
                                    studentLesson.setLessonUrl(lesson.getLessonUrl());
                                    studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                                }
                            } else {
                                // 普通课、试听课
                                studentLesson.setLessonId(lesson.getLessonId());
                                studentLesson.setLessonUrl(lesson.getLessonUrl());
                                int resultCode = studentLessonMapper.insertSelective(studentLesson);

                                if (resultCode > 0) {

                                    // 修改用户公开课剩余可预约课时数
                                    StudentPeriods tempStudentPeriods = talkUserService.getStudentPeriods(userId, periodType);
                                    if (tempStudentPeriods != null) {
                                        int vaildPreviwPeriods = tempStudentPeriods.getVaildPreviwPeriods();
                                        vaildPreviwPeriods = Math.max(vaildPreviwPeriods - 1, 0);
                                        tempStudentPeriods.setVaildPreviwPeriods(vaildPreviwPeriods);
                                        talkUserService.modifyStudentPeriods(tempStudentPeriods);
                                    }

                                    PublishLesson publishLesson = new PublishLesson();
                                    publishLesson.setPeriodId(studentLesson.getPeriodId());
                                    publishLesson.setOrderNum(1);
                                    publishLesson.setState(2);
                                    publishLesson.setStudentId(userId);
                                    publishLessonMapper.updateByPrimaryKeySelective(publishLesson);

                                    // 12小时内预约的课程，初始化调度数据中
                                    if (studentLesson.getBeginTime().getTime() - new Date().getTime() <= 12 * 60 * 60 * 1000) {
                                        try {
                                            // 保存每天待上课老师
                                            LessonRedisSource.addDailyTeacherSet(studentLesson.getTeacherId());
                                            // 保存每天待上课老师对应课程 按开始时间排序
                                            LessonRedisSource.addTeacherDailyLessonSet(studentLesson.getTeacherId(), studentLesson.getPeriodId(), studentLesson.getBeginTime());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            try {
                                hadoopLogger.info("userId:" + userId + ",refresh order student_lesson periodId:"+studentLesson.getPeriodId()+",lessonId:"+studentLesson.getLessonId()+",beginTime:"+DateUtil.formatDate(studentLesson.getBeginTime(), "yyyy-MM-dd HH:mm:ss")+",lessonLevel:"+lesson.getLessonLevel()+",subLevel:"+lesson.getSubLevel());
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } else {
                addPublicLesson(publishLessonList, userId, orderUser);
            }
        } else {
            addTryLesson(publishLessonList, userId, orderUser);
        }
        return tagCode;
    }


    /**
     * 获取学生下一个课程信息
     *
     * @param userLevel
     * @param userId
     * @param studentSubLevel
     * @param lastBeginTime
     * @return
     */
    public Lesson getNextOrderLesson(Integer userLevel, Integer studentSubLevel, Integer userId, Date lastBeginTime, Integer consType, List<LessonLevel> levelList, Map<Integer, Lesson> map) {
        String tagCode = TalkTagCodeEnum.SUCCESS;
        Lesson lesson = new Lesson();
        if (userId != null && userLevel != null) {
            // 获取最后预约课程ID
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("studentId", userId);
            paramMap.put("stateList", Arrays.asList(0, 1, 2));
            paramMap.put("consType", consType);
            if (lastBeginTime != null) {
                paramMap.put("lastBeginTime", lastBeginTime);
            }
            StudentLesson studentLession = studentLessonMapper.getLastOrderLesson(paramMap);
            int subLevel = 0;
            if (studentLession != null) {

                hadoopLogger.info("userId:" + userId + ",getLastOrderLesson :"+GSON.toJson(studentLession)+", queryParam:"+GSON.toJson(paramMap));
                // 获取学生最后预约课程信息
                Lesson laseLesson = talkLessonService.getLessonById(studentLession.getLessonId());
                if (laseLesson != null) {
                    int parentLevel = laseLesson.getLessonLevel();
                    // 当前最后一节课大等级和用户大等级不相同，表示有手工调整等级，按用户大等级排课
                    if (userLevel == parentLevel) {
                        subLevel = laseLesson.getSubLevel();
                    }
                }
                // 验证当前开始时间最后结束的是否有异常
                if (studentLession.getLessonState() == 2 && subLevel > 0) {
                    int periodId = studentLession.getPeriodId();
                    int teacherId = studentLession.getTeacherId();
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("studentId", userId);
                    param.put("periodId", periodId);
                    param.put("teacherId", teacherId);
                    StudentCheckin studentCheckin = studentCheckinMapper.selectByPeriodAndStudentId(param);
                    TeacherCheckin teacherCheckin = teacherCheckinMapper.selectByPeriodAndTeacherId(param);
                    if (studentCheckin != null || teacherCheckin != null) {
                        if (studentCheckin != null) {
                            int studentAbnormalState = studentCheckin.getAbnormalState();
                            // 3：旷课 5：严重迟到 6:早退 7:异常 8:免责请假' 当前课程等级不加
                            if (studentAbnormalState >= 5 || studentAbnormalState == 3) {
                                subLevel = 0;
                            }
                        }
                        if (teacherCheckin != null) {
                            int teacherAbnormalState = teacherCheckin.getAbnormalState();
                            // 3：旷课 6:早退 7:异常 8:严重迟到+1' 当前课程等级不加
                            if (teacherAbnormalState > 5 || teacherAbnormalState == 3) {
                                subLevel = 0;
                            }
                        }
                    }else{
                        // 大等级相同，并且当前用户小等级降级
                        if (subLevel > studentSubLevel) {
                            subLevel = studentSubLevel;
                        }
                    }
                }
            }

            // 有预约记录通过循环课程等级，计算下一等级
            int nextLevel = -1;
            int currentOrderLevel = -1;
            for (LessonLevel lessonLevel : levelList) {
                currentOrderLevel = lessonLevel.getLevelId();
                if (currentOrderLevel > Math.max(subLevel, studentSubLevel)) {
                    nextLevel = currentOrderLevel;
                    break;
                }
            }

            hadoopLogger.info("userId:" + userId + ",lastSubLevel :"+subLevel+",nextSubLevel:"+nextLevel);
            if (nextLevel == -1) {
                LOGGER.info("TalkOrderService.orderLesson getNextOrderLesson lessonlevel:[" + userLevel + "],lessonType:[" + userLevel + "],subLevel:[" + subLevel + "] is last level");
                tagCode = TalkTagCodeEnum.LEVEL_RELA_LESSON_ORDER_OVER;
            }
            // 通过等级获取课程ID
            if (map.containsKey(nextLevel)) {
                lesson = map.get(nextLevel);
            }
            hadoopLogger.info("userId:" + userId + ",getNextOrderLesson :"+GSON.toJson(lesson));
        } else {
            LOGGER.info("TalkOrderService.orderLesson getNextOrderLesson.getLessonLevelByLevel parameter userLevel：[" + userLevel + "], userId：[" + userId + "],exception");
            tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
        }
        lesson.setTagCode(tagCode);
        return lesson;
    }

    @Override
    @Transactional
    public String cancleOrder(Integer userId, Integer periodId, String cancleReason, Integer cancleUser) {
        String tagCode = TalkTagCodeEnum.SUCCESS;
        int cancelPeriodId = 0;
        if (userId != null && periodId != null && cancleUser != null) {
            boolean auth = false;
            // 校验是否有取消约课权限
            TlkAdminService tlkAdminService = MelotBeanFactory.getBean("tlkAdminService", TlkAdminService.class);
            ResultObject resultObject = tlkAdminService.getHavePrivilegeByActionName(cancleUser, "cancelOrder");
            if(resultObject.isSuccess()&&resultObject.getData()!=null){
                List<PrivilegeInfo> list= (List<PrivilegeInfo>) resultObject.getData();
                if (list.size() > 0) {
                    auth = true;
                }
            }

            UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
            // 默认取消预约状态 教务班主任取消
            int lessonState = 3;
            if (userInfo != null) {
                PublishLesson publishLesson = publishLessonMapper.selectByPrimaryKey(periodId);

                if (publishLesson != null) {
                    // 此处修改当前预约信息，修改课程对应的发布信息。
                    int publishType = 1;
                    //课程类型  普通，公开，调试，试听
                    int consType = 1;
                    if (publishLesson.getPublishType() != null) {
                        publishType = publishLesson.getPublishType();
                    }
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("stateList", Arrays.asList(0));
                    int accountType = userInfo.getAccountType();
                    if (accountType == 1) {
                        StudentInfo studentInfo = talkUserService.getStudentInfoByUserId(userId);
                        if (studentInfo != null) {
                            Map<String, Object> queryMap = new HashMap<String, Object>();
                            queryMap.put("studentId", studentInfo.getUserId());
                            queryMap.put("periodId", publishLesson.getPeriodId());
                            StudentLesson orderLesson = studentLessonMapper.selectByUserIdAndPeriodId(queryMap);
                            if (orderLesson != null) {
                                consType = orderLesson.getConsType();
                            }

                            int userType = studentInfo.getUserType();
                            // userId和cancleUser 不相同，并且cancleUser不等于对应CR
                            if (userId.intValue() != cancleUser.intValue()) {
                                if (userType < 1) {
                                    // userId和orderUser 不相同，并且cancleUser不等于对应CR
                                    if ((studentInfo.getCcId() == null || studentInfo.getCcId().intValue() != cancleUser.intValue()) && !auth) {
                                        return TalkTagCodeEnum.UNAUTHORIZED;
                                    }
                                } else {
                                    // userId和orderUser 不相同，并且cancleUser不等于对应CR
                                    if ((studentInfo.getCrId() == null || studentInfo.getCrId().intValue() != cancleUser.intValue()) && !auth) {
                                        return TalkTagCodeEnum.UNAUTHORIZED;
                                    }
                                }
                            }
                            if (publishType == TalkCommonEnum.PUBLISH_TYPE_GENNEL) {
                                if (studentInfo.getUserType() < 1 || consType == 4) {
                                    publishType = TalkCommonEnum.PERIOD_TYPE_DEMO;
                                }
                            }
                            paramMap.put("publishType", publishType);
                            paramMap.put("studentId", userId);
                        }
                    } else {
                        if (accountType == 2) {
                            // userId和cancleUser 不相同，并且cancleUser不等于对应CR
                            if (userId.intValue() != cancleUser.intValue() && !auth) {
                                return TalkTagCodeEnum.UNAUTHORIZED;
                            } else {
                                // 验证取消课程是否此老师发布
                                if (publishLesson.getTeacherId().intValue() != userId.intValue() && !auth) {
                                    return TalkTagCodeEnum.UNAUTHORIZED;
                                }
                            }
                            if (publishType == TalkCommonEnum.PUBLISH_TYPE_GENNEL) {
                                Map<String, Object> param = new HashMap<String, Object>();
                                param.put("teacherId", userId);
                                param.put("periodId", periodId);
                                StudentLesson studentLesson = studentLessonMapper.selectByUserIdAndPeriodId(param);
                                if (studentLesson != null) {
                                    paramMap.put("publishType", studentLesson.getConsType());
                                    paramMap.put("studentId", studentLesson.getStudentId());
                                    publishType=studentLesson.getConsType();
                                }
                            } else {
                                paramMap.put("publishType", TalkCommonEnum.PUBLISH_TYPE_PUBLIC);
                                paramMap.put("teacherId", userId);
                                paramMap.put("periodList", Arrays.asList(periodId));
                            }
                        } else {
                            AdminInfo adminInfo = talkUserService.getAdminInfoByUserId(userId);
                            int adminId = 0;
                            if (adminInfo != null) {
                                adminId = adminInfo.getAdminId();
                            }
                            if (publishType == 3) {
                                // userId和cancleUser 不相同，并且cancleUser不等于对应CR
                                if (adminId != cancleUser.intValue()) {
                                    return TalkTagCodeEnum.UNAUTHORIZED;
                                } else {
                                    // 验证取消课程是否此老师发布
                                    if (publishLesson.getTeacherId().intValue() != userId.intValue()) {
                                        return TalkTagCodeEnum.UNAUTHORIZED;
                                    }
                                }
                                paramMap.put("publishType", TalkCommonEnum.PERIOD_TYPE_TRY);
                                paramMap.put("teacherId", userId);
                                paramMap.put("periodList", Arrays.asList(periodId));
                            } else {
                                if (!auth) {
                                    return TalkTagCodeEnum.UNAUTHORIZED;
                                }
                            }
                        }
                    }
                    // UserType 0:试听用户 1:普通用户 2:vip用户
                    // 计算已预约未结束的课程
                    List<OrderLesson> list = studentLessonMapper.getOrderLesson(paramMap);
                    if (list != null && list.size() > 0) {
                        // 把当前课程ID
                        Collections.sort(list, new Comparator<OrderLesson>() {

                            @Override
                            public int compare(OrderLesson o1, OrderLesson o2) {
                                long objTime1 = o1.getBeginTime().getTime();
                                long objTime2 = o2.getBeginTime().getTime();
                                if (objTime2 < objTime1) {
                                    return 1;
                                } else if (objTime2 > objTime1) {
                                    return -1;
                                }
                                return 0;
                            }
                        });

                        boolean updateTag = false;
                        Integer lessonId = null;
                        int periodIdKey = 0;
                        StudentLesson studentLesson = null;
                        int studentId = 0;
                        int teacherId = 0;

                        for (OrderLesson orderLesson : list) {
                            periodIdKey = orderLesson.getPeriodId();
                            studentId = orderLesson.getStudentId();
                            teacherId = orderLesson.getTeacherId();

                            if (periodIdKey == periodId.intValue()) {
                                updateTag = true;
                                int orderNum = 0;
                                // 公开课不修改periodId 只减少对应orderNum
                                if (publishType == TalkCommonEnum.PERIOD_TYPE_PUBLIC) {
                                    if (publishLesson.getOrderNum() != null) {
                                        orderNum = publishLesson.getOrderNum();
                                        if (orderNum > 0) {
                                            orderNum = orderNum - 1;
                                        } else {
                                            publishLesson.setState(1);
                                        }
                                    }
                                    publishLesson.setOrderNum(orderNum);
                                    publishLessonMapper.updateByPrimaryKeySelective(publishLesson);
                                } else {
                                    if (publishType == TalkCommonEnum.PERIOD_TYPE_TRY) {
                                        publishLessonMapper.deleteByPrimaryKey(periodIdKey);
                                    } else {
                                        // 老师取消预约，直接删除发布记录
                                        if (accountType == 2) {
                                            publishLessonMapper.deleteByPrimaryKey(periodIdKey);
                                        } else {
                                            // 学生取消预约，修改预约状态。
                                            PublishLesson tempPublishLesson = new PublishLesson();
                                            BeanUtils.copyProperties(publishLesson, tempPublishLesson);
                                            tempPublishLesson.setStudentId(null);
                                            tempPublishLesson.setState(1);
                                            tempPublishLesson.setOrderNum(0);
                                            if (publishLessonMapper.insertSelective(tempPublishLesson) > 0) {
                                                cancelPeriodId = tempPublishLesson.getPeriodId();
                                                publishLessonMapper.deleteByPrimaryKey(periodIdKey);
                                            }
                                        }
                                    }
                                }

                                // 调试课
                                int resultCode = 0;
                                if (publishType == TalkCommonEnum.PERIOD_TYPE_TRY) {
                                    TurnStudentLesson turnStudentLesson = new TurnStudentLesson();
                                    turnStudentLesson.setHistId(orderLesson.getHistId());
                                    turnStudentLesson.setTrunVideoState(lessonState);
                                    resultCode = turnStudentLessonMapper.updateByPrimaryKeySelective(turnStudentLesson);
                                } else {
                                    studentLesson = new StudentLesson();
                                    studentLesson.setHistId(orderLesson.getHistId());
                                    studentLesson.setLessonState(lessonState);
                                    StudentInfo studentInfo = userStudentMapper.selectByPrimaryKey(userId);
                                    if(studentInfo != null){
                                        studentLesson.setType(1);
                                    } else {
                                        studentLesson.setType(0);
                                    }
                                    studentLesson.setCancleUser(cancleUser);
                                    studentLesson.setCancleTime(new Date());
                                    studentLesson.setCancleReason(cancleReason);
                                    resultCode = studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                                }

                                if (resultCode > 0) {
                                    // 修改用户考勤信息
                                    recordCancleOrder(userId, studentId, teacherId, periodId, publishType, orderLesson.getBeginTime(), cancleReason, accountType);
                                }

                                // 12小时内预约的课程，初始化调度数据中
                                if (orderLesson.getBeginTime().getTime() - new Date().getTime() <= 12 * 60 * 60 * 1000) {
                                    try {
                                        // 保存每天待上课老师对应课程 按开始时间排序
                                        LessonRedisSource.removeTeacherDailyLessonSet(String.valueOf(orderLesson.getTeacherId()), String.valueOf(orderLesson.getPeriodId()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            // 修改标记true表示，后面的预约课程ID顺延
                            if (updateTag) {
                                if (orderLesson.getPublishType() == TalkCommonEnum.PERIOD_TYPE_DEMO || orderLesson.getPublishType() == TalkCommonEnum.PERIOD_TYPE_GENNEL) {
                                    if (lessonId != null) {
                                        Lesson lesson = talkLessonService.getLessonById(lessonId);
                                        studentLesson = new StudentLesson();
                                        studentLesson.setHistId(orderLesson.getHistId());
                                        studentLesson.setLessonId(lessonId);
                                        studentLesson.setLessonUrl(lesson.getLessonUrl());
                                        studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                                        try {
                                            hadoopLogger.info("userId:" + userId + ",refresh cancleOrder student_lesson periodId:"+orderLesson.getPeriodId()+",lessonId:"+lessonId+",beginTime:"+DateUtil.formatDate(orderLesson.getBeginTime(), "yyyy-MM-dd HH:mm:ss")+",lessonLevel:"+lesson.getLessonLevel()+",subLevel:"+lesson.getSubLevel());
                                        } catch (Exception e) {
                                        }
                                    }
                                    lessonId = orderLesson.getLessonId();
                                }
                            }
                        }
                    }
                    // 预约取消成功,发送邮件通知
                    adviceAfterCancel(cancelPeriodId, accountType, userId);
                } else {
                    LOGGER.error("LessonServiceImpl.cancleOrder periodId:[" + periodId + "] query table action_teacher_public_freetime is null");
                    tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
                }
            } else {
                LOGGER.error("LessonServiceImpl.cancleOrder userId:[" + userId + "] query table info_user_register is null");
                tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
            }
        } else {
            LOGGER.error("LessonServiceImpl.cancleOrder userId:[" + userId + "],periodId:[" + periodId + "],cancleUser:[" + cancleUser + "] is null");
            tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
        }
        return tagCode;
    }

    /**
     * 取消预约后发送邮件通知
     *
     * @param periodId
     * @param accountType
     * @param userId
     */
    private void adviceAfterCancel(Integer periodId, int accountType, int userId) {

        boolean inFiveMin = LessonRedisSource.existEmailAdvice(periodId);
        List<Integer> studentIdList = new ArrayList<>();
        int adviceType = 0;
        // 学生取消,并且不再
        if (accountType == 1) {
            studentIdList.add(userId);
            if (inFiveMin) {
                adviceType = EmailAdviceTypeEnum.STUDENT_CANCEL_ORDER_IN_FIVE_MIN;
            } else {
                adviceType = EmailAdviceTypeEnum.STUDENT_CANCEL_ORDER;
            }
        }
        // 老师取消,批量通知学生
        else if (adviceType == 2) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("teacherId", userId);
            params.put("periodList", Arrays.asList(periodId));
            List<OrderLesson> lessonList = studentLessonMapper.getOrderLesson(params);
            for (OrderLesson lesson : lessonList) {
                studentIdList.add(lesson.getStudentId());
            }
            if (inFiveMin) {
                adviceType = EmailAdviceTypeEnum.TEACHER_CANCEL_ORDER_IN_FIVE_MIN;
            } else {
                adviceType = EmailAdviceTypeEnum.TEACHER_CANCEL_ORDER;
            }
        } else {
            adviceType = EmailAdviceTypeEnum.STUDENT_CANCEL_ORDER;
        }

        for (int studentId : studentIdList) {
            talkEmailService.sendTemplateMail(adviceType, periodId, studentId, 0);
        }
    }

    /**
     * 记录取消预约信息
     * @param userId
     * @param studentId
     * @param teacherId
     * @param periodId
     * @param publishType
     * @param beginTime
     * @param cancleReason
     */
    public void recordCancleOrder(Integer userId, Integer studentId, Integer teacherId, Integer periodId, Integer publishType, Date beginTime, String cancleReason, Integer accountType) {
        int abnormalState = 1;
        int needIncr = 0, validIncr = 0, realIncr = 0, extraIncr = 0;
        int periodType = 1;
        if (publishType == TalkCommonEnum.PERIOD_TYPE_PUBLIC) {
            periodType = 2;
        } else if (publishType == TalkCommonEnum.PERIOD_TYPE_GENNEL) {
            ;
        }else if (publishType == TalkCommonEnum.PERIOD_TYPE_DEMO) {
            periodType = 3;
        }
        long cancleTime = beginTime.getTime() - new Date().getTime();
        // 小于12消息，计算免责请假
        if (cancleTime < 12 * 60 * 60 * 1000) {
            // 请假考勤状态
            abnormalState = 2;
            // 修改用户考勤信息
            CheckinInfo checkinInfo = talkUserService.getCheckinInfo(userId);
            int allFreeAskForLeave = 0;
            int freeAskForLeave = 0;
            if (checkinInfo != null) {
                // 允许免责请假次数
                allFreeAskForLeave = checkinInfo.getAllFreeAskForLeave() == null ? 0 : checkinInfo.getAllFreeAskForLeave();
                // 免责请假次数
                freeAskForLeave = checkinInfo.getFreeAskForLeave() == null ? 0 : checkinInfo.getFreeAskForLeave();
            }
            StudentPeriods studentPeriods = talkUserService.getStudentPeriods(studentId, periodType);
            if (studentPeriods != null) {
                // 只有学生有免责请假，并且在允许免责请假次数范围内
                if (accountType == 1 && freeAskForLeave < allFreeAskForLeave && publishType < 3) {
                    if (checkinInfo != null) {
                        checkinInfo.setFreeAskForLeave(freeAskForLeave + 1);
                        talkUserService.modifyCheckinInfo(checkinInfo);
                    }
                    // 免责请假，补回学生剩余可预约数
                    Integer vaildPreviwPeriods = studentPeriods.getVaildPreviwPeriods();
                    if (vaildPreviwPeriods == null) {
                        vaildPreviwPeriods = 0;
                    }
                    studentPeriods.setVaildPreviwPeriods(vaildPreviwPeriods + 1);
                    abnormalState = 8;
                    // 老师当前课时有效
                    validIncr = 1;
                } else {
                    if (checkinInfo != null) {
                        // 非免责请假，扣课请假
                        checkinInfo.setPeriodsAskForLeave((checkinInfo.getPeriodsAskForLeave() == null ? 0 : checkinInfo.getPeriodsAskForLeave()) + 1);
                        talkUserService.modifyCheckinInfo(checkinInfo);
                    }
                    // 学生扣课请假，扣除剩余有效课时
                    if (accountType == 1) {
                        // 扣除课程剩余数、可预约课程数
                        int curPeriods = studentPeriods.getCurPeriods();
                        if (curPeriods > 0) {
                            studentPeriods.setCurPeriods(curPeriods - 1);
                        }
                        // 老师当前课时有效
                        validIncr = 1;
                    } else {
                        // 老师非免责请假，当前课时减1,给学生补一节有效课时
                        studentPeriods.setCurPeriods(studentPeriods.getCurPeriods() + 1);
                        studentPeriods.setVaildPreviwPeriods(studentPeriods.getVaildPreviwPeriods() + 2);

                        // 老师当前课时无效，扣除有效课时
                        extraIncr = 1;
                        needIncr = 1;
                    }
                }
                // 调试课 对应学生课时不做处理
                if (publishType != TalkCommonEnum.PERIOD_TYPE_TRY) {
                    talkUserService.modifyStudentPeriods(studentPeriods);
                }
            }
        } else {
            // 普通请假，补回学生剩余可预约数
            StudentPeriods studentPeriods = talkUserService.getStudentPeriods(studentId, periodType);
            if (studentPeriods != null) {
                Integer vaildPreviwPeriods = studentPeriods.getVaildPreviwPeriods();
                if (vaildPreviwPeriods == null) {
                    vaildPreviwPeriods = 0;
                }
                studentPeriods.setVaildPreviwPeriods(vaildPreviwPeriods + 1);
                studentPeriods.setAccumulativeTotal(25 * 60 * 1000 * (studentPeriods.getOverPeriods()));

                // 调试课 对应学生课时不做处理
                if (publishType != TalkCommonEnum.PERIOD_TYPE_TRY) {
                    studentPeriodsMapper.updateByPrimaryKeySelective(studentPeriods);
                }
            }
        }
        // 修改老师课时统计
        if (publishType != TalkCommonEnum.PERIOD_TYPE_TRY) {
            updateTeacherPeriod(teacherId, publishType, periodId, needIncr, validIncr, realIncr, extraIncr, new Date());
        }
        // 保存用户取消预约考勤
        if (accountType == 1) {
            StudentCheckin studentCheckin = new StudentCheckin(userId, periodId, abnormalState, cancleReason);
            studentCheckinMapper.insertSelective(studentCheckin);
        } else {
            TeacherCheckin teacherCheckin = new TeacherCheckin(userId, periodId, abnormalState, cancleReason);
            teacherCheckinMapper.insertSelective(teacherCheckin);
        }
    }

    /**
     * 修改老师当前课程 汇总信息
     * @param periodId
     * @param validIncr 有效课时
     * @param realIncr 实上课时
     * @param extraIncr 需补课时
     */
    public void updateTeacherPeriod(int teacherId, int lessonType, int periodId, int needIncr, int validIncr, int realIncr, int extraIncr, Date dTime) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("teacherId", teacherId);

        TeacherPerformance lessonHist = teacherLessonHistMapper.selectByPeriodId(periodId);
        if (lessonHist != null) {

            int needPeriod = lessonHist.getNeedPeriods();
            int validPeriod = lessonHist.getVaildPeriods();
            int realPeriod = lessonHist.getRealPeriods();
            int extraPeriod = lessonHist.getExtraPeriods() == null ? 0 : lessonHist.getExtraPeriods();

            // 当前数据回退
            param.put("dtime", lessonHist.getDtime());
            // 老师状态
            if (extraIncr != 0 && validIncr == 0) {
                // 老师严重迟到、旷课、课程异常、紧急请假 数据回退
                validPeriod = validIncr;
                extraPeriod = extraPeriod + extraIncr;
                if (extraIncr > 0) {
                    // 查询当前时间往后最近一条，补课记录，修改有效课时，需补课时
                    param.put("extraPeriods", 0);
                    param.put("vaildPeriods", 1);
                    TeacherPerformance teacherPerformance = teacherLessonHistMapper.getLateByMap(param);
                    if (teacherPerformance != null) {
                        teacherPerformance.setExtraPeriods(-1);
                        teacherPerformance.setVaildPeriods(0);
                        teacherLessonHistMapper.updateByPrimaryKeySelective(teacherPerformance);
                    }
                } else if (extraIncr < 0) {
                    // 查询当前时间往后最近一条，补课记录，修改有效课时，需补课时
                    param.put("extraPeriods", -1);
                    param.put("vaildPeriods", 0);
                    TeacherPerformance teacherPerformance = teacherLessonHistMapper.getLateByMap(param);
                    if (teacherPerformance != null) {
                        teacherPerformance.setExtraPeriods(0);
                        teacherPerformance.setVaildPeriods(1);
                        teacherLessonHistMapper.updateByPrimaryKeySelective(teacherPerformance);
                    }
                }
                // 学生状态
            } else if (extraIncr == 0 && validIncr != 0) {
                // 学生旷课、严重迟到、早退、课程异常回退
                if (validIncr < 0) {
                    // 表示当前抵消上一次待补课时
                    if (validPeriod == 0 && extraPeriod == -1) {
                        validPeriod = 0;
                        extraPeriod = 0;

                        // 查询当前时间往后最近一条，补课记录，修改有效课时，需补课时
                        param.put("extraPeriods", 0);
                        param.put("vaildPeriods", 1);
                        TeacherPerformance teacherPerformance = teacherLessonHistMapper.getLateByMap(param);
                        if (teacherPerformance != null) {
                            teacherPerformance.setExtraPeriods(-1);
                            teacherPerformance.setVaildPeriods(0);
                            teacherLessonHistMapper.updateByPrimaryKeySelective(teacherPerformance);
                        }
                    } else if (validPeriod > 0 && extraPeriod == 0) {
                        validPeriod = validPeriod + validIncr;
                    }
                    // 学生旷课、严重迟到、早退、课程异常
                } else if (validIncr > 0) {
                    // 检测在此之前是否有需补课时
                    validPeriod = validPeriod + validIncr;
                    // 计算当前课时之前，老师是否有需补课时
                    TeacherOverPeriodCount teacherOverPeriodCount = teacherLessonHistMapper.selectSumByTeacherAndDtime(param);
                    if (teacherOverPeriodCount != null) {
                        // 当前没有需补课时
                        if (teacherOverPeriodCount.getExtraPeriods() > 0) {
                            // 当前有需补课时
                            validPeriod = 0;
                            extraPeriod = -1;

                            // 查询当前时间往后最近一条，补课记录，修改有效课时，需补课时
                            param.put("extraPeriods", -1);
                            param.put("vaildPeriods", 0);
                            TeacherPerformance teacherPerformance = teacherLessonHistMapper.getLateByMap(param);
                            if (teacherPerformance != null) {
                                teacherPerformance.setExtraPeriods(0);
                                teacherPerformance.setVaildPeriods(1);
                                teacherLessonHistMapper.updateByPrimaryKeySelective(teacherPerformance);
                            }
                        }
                    }
                }
            }
            lessonHist.setExtraPeriods(extraPeriod);
            lessonHist.setNeedPeriods(needPeriod + needIncr);
            lessonHist.setRealPeriods(realPeriod + realIncr);
            lessonHist.setVaildPeriods(validPeriod);
            teacherLessonHistMapper.updateByPrimaryKey(lessonHist);
        } else {
            param.put("dtime", dTime);
            // 统计是否还有需补课时
            if (validIncr > 0) {
                TeacherOverPeriodCount teacherOverPeriodCount = teacherLessonHistMapper.selectSumByTeacherAndDtime(param);
                if (teacherOverPeriodCount != null) {
                    int extraPeriords = teacherOverPeriodCount.getExtraPeriods();
                    if (extraPeriords > 0) {
                        // 扣除需补课时，当前有效课时为0
                        extraIncr = validIncr * -1;
                        validIncr = 0;
                    }
                }
            }
            lessonHist = new TeacherPerformance();
            lessonHist.setDtime(dTime);
            lessonHist.setExtraPeriods(extraIncr);
            lessonHist.setLessonType(lessonType);
            lessonHist.setNeedPeriods(needIncr);
            lessonHist.setPeriodId(periodId);
            lessonHist.setRealPeriods(realIncr);
            lessonHist.setTeacherId(teacherId);
            lessonHist.setVaildPeriods(validIncr);
            teacherLessonHistMapper.insertSelective(lessonHist);
        }
    }

    /**
     * 通过查询状态转换成对应课程状态
     *
     * @param queryState
     * @return
     */
    public List<Integer> getOrderLessonState(Integer queryState) {
        List<Integer> stateList = new ArrayList<Integer>();
        if (queryState != null) {
            if (queryState.intValue() == 0) {
                stateList.add(0);
            } else if (queryState.intValue() == 1) {
                stateList.add(1);
            } else if (queryState.intValue() == 2) {
                stateList.add(2);
            } else if (queryState.intValue() == 3) {
                stateList.add(3);
            } else if (queryState.intValue() == 4) {
                stateList.add(4);
            } else if (queryState.intValue() == 7) {   //已上课
                stateList.add(2);
                stateList.add(3);
                stateList.add(4);
            } else if (queryState.intValue() == 8) {   //待上课
                stateList.add(0);
                stateList.add(1);
            }
        }
        return stateList;
    }

    @Override
    public Pager<OrderLesson> getPagerOrderLessonList(Integer userId, Integer lessonState, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset, String order) {
        if (userId == null) {
            return null;
        }
        UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        int accountType = userInfo.getAccountType();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);

        paramMap.put("publishType",publishType);

        if (accountType == 1) {
            // 学生查询学生课程表
            paramMap.put("studentId", userId);
        } else {
            // 老师直接查询发布信息表
            paramMap.put("teacherId", userId);
        }
        if (lessonState != null) {
            paramMap.put("stateList", getOrderLessonState(lessonState));
        }
        Pager<OrderLesson> pager = new Pager<OrderLesson>();
        pager.setStart(start);
        pager.setPageSize(offset);
        pager.setParams(paramMap);
        pager.setOrder(order);

        List<OrderLesson> list = studentLessonMapper.findPager(pager);

        if (list != null && list.size() > 0) {
            pager.setPageItems(formatOrderLessonList(list));
        }
        return pager;
    }

    @Override
    public Pager<OrderLesson> getOverLessonList(Integer userId, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset, String order) {
        if (userId == null) {
            return null;
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);

        paramMap.put("publishType", publishType);

        // 老师直接查询发布信息表
        paramMap.put("teacherId", userId);


        Pager<OrderLesson> pager = new Pager<OrderLesson>();
        pager.setStart(start);
        pager.setPageSize(offset);
        pager.setParams(paramMap);
        pager.setOrder(order);

        List<OrderLesson> list = studentLessonMapper.findOverPager(pager);

        if (list != null && list.size() > 0) {
            pager.setPageItems(formatOrderLessonList(list));
        }
        return pager;
    }

    @Override
    public Pager<OrderLesson> getLeaveLessonList(Integer userId, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset, String order) {
        if (userId == null) {
            return null;
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);

        paramMap.put("publishType", publishType);

        // 老师直接查询发布信息表
        paramMap.put("teacherId", userId);


        Pager<OrderLesson> pager = new Pager<OrderLesson>();
        pager.setStart(start);
        pager.setPageSize(offset);
        pager.setParams(paramMap);
        pager.setOrder(order);

        List<OrderLesson> list = studentLessonMapper.findLeavePager(pager);

        if (list != null && list.size() > 0) {
            pager.setPageItems(formatOrderLessonList(list));
        }
        return pager;
    }

    @Override
    public Pager<OrderLesson> getAwaitLessonList(Integer userId, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset, String order) {
        if (userId == null) {
            return null;
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);

        paramMap.put("publishType", publishType);

        // 老师直接查询发布信息表
        paramMap.put("teacherId", userId);


        Pager<OrderLesson> pager = new Pager<OrderLesson>();
        pager.setStart(start);
        pager.setPageSize(offset);
        pager.setParams(paramMap);
        pager.setOrder(order);

        List<OrderLesson> list = studentLessonMapper.findAwaitPager(pager);

        if (list != null && list.size() > 0) {
            pager.setPageItems(formatOrderLessonList(list));
        }
        return pager;
    }

    /**
     * 通过mybatis关联查询，需要类型转换
     * @param list
     * @return
     */
    private List<OrderLesson> formatOrderLessonList(List<OrderLesson> list) {
        if (list != null && list.size() > 0) {
            List<OrderLesson> tempList = new ArrayList<OrderLesson>();
            for (OrderLesson orderLesson : list) {
                tempList.add(formatOrderLesson(orderLesson));
            }
            return tempList;
        }
        return null;
    }

    private OrderLesson formatOrderLesson(OrderLesson orderLesson) {
        if (orderLesson != null) {
            OrderLesson lesson = new OrderLesson();
            BeanUtils.copyProperties(orderLesson, lesson);
            return lesson;
        }
        return null;
    }

    @Override
    public List<OrderLesson> getOrderLessonList(Integer userId, Integer lessonState, Date beginTime, Date endTime, Integer publishType) {
        if (userId == null) {
            return null;
        }
        UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        int accountType = userInfo.getAccountType();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);

        if (accountType == 1) {
            // 学生查询学生课程表
            paramMap.put("studentId", userId);
        } else if (accountType == 2) {
            // 老师直接查询发布信息表
            paramMap.put("teacherId", userId);
        }
        if (lessonState != null) {
            paramMap.put("stateList", getOrderLessonState(lessonState));
        }

        List<OrderLesson> list = studentLessonMapper.getOrderLesson(paramMap);
        return formatOrderLessonList(list);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkLessonService#getOrderLessonByPeriodList
     * (java.util.List)
     */
    @Override
    public List<OrderLesson> getOrderLessonByPeriodList(List<Integer> periodList) {
        if (periodList != null && periodList.size() > 0) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("periodList", periodList);
            List<OrderLesson> list = studentLessonMapper.getOrderLesson(paramMap);

            return formatOrderLessonList(list);
        }
        return null;
    }

    @Override
    public TeacherOverPeriodCount getTeacherOverPeriodCount(Integer teacherId) {
        if (teacherId != null) {
            if (talkUserService.getTeacherInfoByUserId(teacherId) != null) {
                TeacherOverPeriodCount periodCount = teacherLessonHistMapper.selectSum(teacherId);
                return periodCount;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkOrderService#addComment(java.lang
     * .Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @Override
    public int addComment(Integer periodId, Integer studentId, Integer questionId, String comment) {
        if (periodId != null && studentId != null && StringUtils.isNotBlank(comment)) {

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("studentId", studentId);
            paramMap.put("periodId", periodId);
            StudentLesson studentLesson = studentLessonMapper.selectByUserIdAndPeriodId(paramMap);
            if (studentLesson != null) {
                // 详细评论
                if (questionId != null) {
                    DetailCommentQuestion detailCommentQuestion = talkLessonService.getDetailCommentQuestionById(questionId);
                    if (detailCommentQuestion != null) {
                        TeacherDetailComment teacherDetailComment = new TeacherDetailComment();
                        teacherDetailComment.setDetailId(studentLesson.getHistId());
                        teacherDetailComment.setQuestionId(questionId);
                        // 查询是否存在
                        List<TeacherDetailComment> list = teacherDetailCommentMapper.selectBySelective(teacherDetailComment);
                        int resultCode = -1;
                        if (list != null && list.size() > 0) {
                            for (TeacherDetailComment detailComment : list) {
                                detailComment.setDtime(new Date());
                                detailComment.setTeacherAnswer(comment);
                                resultCode = teacherDetailCommentMapper.updateByPrimaryKeySelective(detailComment);
                            }
                        } else {
                            teacherDetailComment.setDtime(new Date());
                            teacherDetailComment.setTeacherAnswer(comment);
                            resultCode = teacherDetailCommentMapper.insertSelective(teacherDetailComment);
                        }
                        if (resultCode > 0) {
                            studentLesson.setIsDetailComment(1);
                            return studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                        }
                    }
                } else {
                    studentLesson.setTeacherCommend(comment);
                    return studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                }
            }
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkOrderService#getDetailCommentList
     * (java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<TeacherDetailComment> getDetailCommentList(Integer periodId, Integer studentId) {
        if (periodId != null && studentId != null) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("studentId", studentId);
            paramMap.put("periodId", periodId);
            StudentLesson studentLesson = studentLessonMapper.selectByUserIdAndPeriodId(paramMap);
            if (studentLesson != null) {
                TeacherDetailComment teacherDetailComment = new TeacherDetailComment();
                teacherDetailComment.setDetailId(studentLesson.getHistId());
                return teacherDetailCommentMapper.selectBySelective(teacherDetailComment);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkOrderService#checkInLessonState(java
     * .lang.Integer)
     */
    @Override
    @Transactional
    public boolean checkinOverLesson(Integer periodId) {
        if (periodId != null) {
            PublishLesson publishLesson = talkPublishService.getPublishLessonById(periodId);
            if (publishLesson != null) {

                // 只处理已结束课程,和已预约的课程
                int publishState = publishLesson.getState();
                Date beginTime = publishLesson.getBeginTime();
                Date endTime = publishLesson.getEndTime();
                if (endTime.getTime() < new Date().getTime() && publishState == 2) {
                    int teacherId = publishLesson.getTeacherId();

                    // 获取该课程对应预约信息
                    List<OrderLesson> orderLessonList = getOrderLessonByPeriodList(Arrays.asList(Integer.valueOf(periodId)));
                    if (orderLessonList != null && orderLessonList.size() > 0) {

                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("teacherId", teacherId);
                        param.put("periodId", periodId);

                        // 获取当前老师对应课程的inout记录
                        ActionLessioning actionLessioning = null;
                        for (OrderLesson orderLesson : orderLessonList) {
                            // 过滤已完结的
                            if (orderLesson.getLessonState() >= 2) {
                                continue;
                            }
                            int studentId = orderLesson.getStudentId();
                            param.put("studentId", studentId);
                            // 查询课程进行状态
                            actionLessioning = actionLessioningMapper.selectByPeriodAndUser(param);

                            if (actionLessioning != null) {
                                actionLessioning.setState(2);
                                actionLessioningMapper.updateByPrimaryKeySelective(actionLessioning);
                            } else {
                                actionLessioning = new ActionLessioning(periodId, studentId, teacherId, orderLesson.getBeginTime(), orderLesson.getEndTime());
                                actionLessioning.setState(2);
                                actionLessioningMapper.insertSelective(actionLessioning);
                            }

                            // 结束当前课程
                            int periodType = orderLesson.getPublishType();
                            // 调试课
                            if (periodType == TalkCommonEnum.PERIOD_TYPE_TRY) {
                                TurnStudentLesson turnStudentLesson = new TurnStudentLesson();
                                turnStudentLesson.setHistId(orderLesson.getHistId());
                                turnStudentLesson.setTrunVideoState(2);
                                turnStudentLessonMapper.updateByPrimaryKeySelective(turnStudentLesson);
                            } else {
                                StudentLesson studentLesson = new StudentLesson();
                                studentLesson.setHistId(orderLesson.getHistId());
                                studentLesson.setLessonState(2);
                                studentLessonMapper.updateByPrimaryKeySelective(studentLesson);

                                // 获取老师、学生进入课程、退出课程时间
                                Date teacherInTime = actionLessioning.getTeacherInTime();
                                Date studentInTime = actionLessioning.getStudentInTime();

                                // 计算老师是否迟到
                                int teacherState = checkIsLate(beginTime, teacherInTime);
                                // 计算学生是否迟到
                                int studentState = checkIsLate(beginTime, studentInTime);

                                // 计算老师和学生课时
                                computePeriod(teacherState, teacherId, studentState, studentId, orderLesson.getLessonId(), beginTime, periodId, periodType,
                                        orderLesson.getHistId(), true);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测是否旷课
     *
     * @param beginTime
     * @param inTime
     * @return 0 ：正常  3：旷课 4：迟到(小于2分钟) 5：严重迟到(大于2分钟小于15分钟) 7:课程异常 8：严重迟到+1(超过15分钟)
     */
    private int checkIsLate(Date beginTime, Date inTime) {
        int lateState = 0;
        if (beginTime != null) {
            if (inTime == null) {
                // TODO 如果没进入教室，按正常处理 旷课
                // lateState = 3;
            } else {
                if (beginTime.getTime() < inTime.getTime()) {
                    long lateTime = inTime.getTime() - beginTime.getTime();
                    // 迟到时间小于2分钟,老师有效课程加1,学生课时减1,
                    if (lateTime <= 2 * 60 * 1000) {
                        lateState = 4;
                    } else if (lateTime > 2 * 60 * 1000 && lateTime <= 15 * 60 * 1000) {
                        lateState = 5;
                    } else {
                        // 超过15分钟严重迟到 +1， 按旷课处理
                        lateState = 8;
                    }
                }
            }
        }
        return lateState;
    }

    /**
     * 计算课时
     *
     * @param teacherState
     * @param teacherId
     * @param studentState
     * @param studentId
     * @param lessonId
     * @param beginTime
     * @return
     */
    private int computePeriod(int teacherState, int teacherId, int studentState, int studentId, int lessonId, Date beginTime, int periodId, int periodType, int studentLessonId, boolean modifyLevelTag) {

        int curPeriods = 0, overPeriods = 0, subLevel = 0, validPeriods = 0, realPeriod = 0, extraPeriod = 0, needPeriod = 1;
        int vaildPreviwPeriods = 0;
        // 老师和学生都正常上课 学生剩余课时减1,完成课时加1,当前用户等级加1,老师有效课时加1
        if (teacherState == 0 && studentState == 0) {
            curPeriods = -1;
            overPeriods = 1;
            subLevel = 1;
            realPeriod = 1;
            validPeriods = 1;
        }
        if (teacherState != 0) {
            if (studentState == 0) {
                String applyDescribe = "迟到";
                // 老师旷课\课程异常、验证迟到+1 学生剩余课时加1,完成课时不变,当前用户等级不变,老师当前课时无效,有效课时减1
                if (teacherState == 3 || teacherState == 7 || teacherState == 8) {
                    curPeriods = 1;
                    vaildPreviwPeriods = modifyLevelTag ? 2 : 1;
                    extraPeriod = 1;
                    if (teacherState == 3) {
                        applyDescribe = "旷课";
                    } else if (teacherState == 7) {
                        applyDescribe = "课程异常";
                    }
                    // 老师旷迟到小于2分钟 学生剩余课时减1,完成课时加1,当前用户等级加1,老师有效课时加1
                } else if (teacherState == 4) {
                    curPeriods = -1;
                    overPeriods = 1;
                    subLevel = 1;
                    validPeriods = 1;
                    realPeriod = 1;
                    // 老师严重迟到小于15分钟,大于2分钟
                    // 学生剩余课时加1,扣一节课时,完成课时加1,当前用户等级加1,老师当前课时无效,有效课时减1
                } else if (teacherState == 5) {
                    overPeriods = 1;
                    subLevel = 1;
                    vaildPreviwPeriods = 1;
                    extraPeriod = 1;
                    realPeriod = 1;
                }
                Integer histId = null;
                if (teacherState > 2 && teacherState < 9) {
                    // 老师状态异常，学生正常，责任归属老师
                    TeacherCheckin teacherCheckin = new TeacherCheckin(teacherId, periodId, teacherState, "");
                    int resultCode = teacherCheckinMapper.insertSelective(teacherCheckin);
                    if (resultCode > 0) {
                        histId = teacherCheckin.getHistId();
                    }
                    CheckinInfo checkinInfo = talkUserService.getCheckinInfo(teacherId);
                    if (checkinInfo != null) {
                        // 旷课
                        if (teacherState == 3) {
                            checkinInfo.setDayOffTimes((checkinInfo.getDayOffTimes() == null ? 0 : checkinInfo.getDayOffTimes()) + 1);
                            // 早退
                        } else if (teacherState == 6) {
                            checkinInfo.setLeaveEarlyTimes((checkinInfo.getLeaveEarlyTimes() == null ? 0 : checkinInfo.getLeaveEarlyTimes()) + 1);
                            // 迟到
                        } else if (teacherState == 4 || teacherState == 5 || teacherState == 8) {
                            checkinInfo.setDelayTimes((checkinInfo.getDelayTimes() == null ? 0 : checkinInfo.getDelayTimes()) + 1);
                        }
                        talkUserService.modifyCheckinInfo(checkinInfo);
                    }
                }

                if (curPeriods > 0) {
                    // 给学生申请一节课时
                    ApplyPeriods applyPeriods = new ApplyPeriods();
                    applyPeriods.setApplyDescribe("老师" + applyDescribe + ",给学生增加1节课时");
                    applyPeriods.setApplyPeriods(curPeriods);
                    applyPeriods.setLinkid(histId);
                    applyPeriods.setApplyTime(new Date());
                    applyPeriods.setUserId(studentId);
                    applyPeriods.setUserType(1);
                    applyPeriods.setState(1);
                    applyPeriodsMapper.insertSelective(applyPeriods);
                }
                if (validPeriods < 0) {
                    // 给给老师处罚，增加一节免费课（相当于有效课时减1）
                    ApplyPeriods applyPeriods = new ApplyPeriods();
                    applyPeriods.setApplyDescribe("老师" + applyDescribe + ",当前有效课时减1");
                    applyPeriods.setApplyPeriods(validPeriods);
                    applyPeriods.setLinkid(histId);
                    applyPeriods.setApplyTime(new Date());
                    applyPeriods.setUserId(teacherId);
                    applyPeriods.setUserType(2);
                    applyPeriods.setState(1);
                    applyPeriodsMapper.insertSelective(applyPeriods);
                }
            }
        }

        if (studentState != 0) {
            // 学生旷课、严重迟到、课程异常 学生剩余课时减1,完成课时不变,当前用户等级不变,老师有效课时加1
            if (studentState == 3 || studentState == 5 || studentState == 7) {
                curPeriods = -1;
                validPeriods = 1;
                // 学生迟到小于15分钟 学生剩余课时减1,完成课时加1,当前用户等级加1,老师有效课时加1
            } else if (studentState == 4) {
                curPeriods = -1;
                overPeriods = 1;
                subLevel = 1;
                validPeriods = 1;
                realPeriod = 1;
            }

            if (studentState > 2 && studentState < 8) {
                StudentCheckin studentCheckin = new StudentCheckin(studentId, periodId, studentState, "");
                studentCheckin.setConsPeriods(1);
                studentCheckinMapper.insertSelective(studentCheckin);
                CheckinInfo checkinInfo = talkUserService.getCheckinInfo(studentId);
                if (checkinInfo != null) {
                    // 旷课
                    if (studentState == 3) {
                        checkinInfo.setDayOffTimes((checkinInfo.getDayOffTimes() == null ? 0 : checkinInfo.getDayOffTimes()) + 1);
                        // 早退
                    } else if (studentState == 6) {
                        checkinInfo.setLeaveEarlyTimes((checkinInfo.getLeaveEarlyTimes() == null ? 0 : checkinInfo.getLeaveEarlyTimes()) + 1);
                        // 迟到
                    } else if (studentState == 4 || studentState == 5) {
                        checkinInfo.setDelayTimes((checkinInfo.getDelayTimes() == null ? 0 : checkinInfo.getDelayTimes()) + 1);
                    }
                    talkUserService.modifyCheckinInfo(checkinInfo);
                }
            }
        }

        if (teacherState == 7 || studentState == 7) {
            StudentLesson studentLesson = new StudentLesson();
            studentLesson.setHistId(studentLessonId);
            studentLesson.setLessonState(4);
            studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", studentId);
        if (periodType == TalkCommonEnum.PERIOD_TYPE_DEMO) {
            param.put("periodsType", 3);
        } else {
            param.put("periodsType", periodType);
        }
        // 查询当前学生课时数
        StudentPeriods studentPeriods = studentPeriodsMapper.selectByUserIdAndPeriodType(param);

        // 表示学生老师都迟到，扣除当前学生剩余课时，上完课程不加，学生等级不加，标记当前预约课程完成，老师课时加一
        if (studentPeriods != null) {
            // 取最大当前剩余课时，不能减未负数
            int tempCurPeriods = 0;
            if (studentPeriods.getCurPeriods() != null) {
                tempCurPeriods = studentPeriods.getCurPeriods();
            }
            curPeriods = Math.max(tempCurPeriods + curPeriods, 0);
            int tempOverPeriods = 0;
            if (studentPeriods.getOverPeriods() != null) {
                tempOverPeriods = studentPeriods.getOverPeriods();
            }
            overPeriods = tempOverPeriods + overPeriods;

            // 当前课时数大于0，表示老师异常补偿学生课时
            if (vaildPreviwPeriods > 0) {
                int tempVaildPreviwPeriods = 0;
                if (studentPeriods.getVaildPreviwPeriods() != null) {
                    tempVaildPreviwPeriods = studentPeriods.getVaildPreviwPeriods();
                }
                studentPeriods.setVaildPreviwPeriods(vaildPreviwPeriods + tempVaildPreviwPeriods);
            }
            // 修改用户等级
            if (subLevel != 0 && modifyLevelTag) {
                // 查询当前完结课程上一个等级
                StudentInfo studentInfo = talkUserService.getStudentInfoByUserId(studentId);
                if (studentInfo != null) {
                    int currentSubLevel = studentInfo.getSubLevel();
                    int userLevel = studentInfo.getUserLevel();
                    if (currentSubLevel + subLevel > 0) {
                        currentSubLevel = currentSubLevel + subLevel;
                    }
                    modifyUserLevel(studentId, userLevel, currentSubLevel);
                }
            }
            studentPeriods.setCurPeriods(curPeriods);
            studentPeriods.setOverPeriods(overPeriods);

            studentPeriods.setAccumulativeTotal(25 * 60 * 1000 * overPeriods);

            studentPeriodsMapper.updateByPrimaryKeySelective(studentPeriods);
        }

        updateTeacherPeriod(teacherId, periodType, periodId, needPeriod, validPeriods, realPeriod, extraPeriod, beginTime);
        return 0;
    }

    /**
     * 扣除用户课时
     *
     * @param teacherId
     * @param studentId
     * @param lessonId
     * @param beginTime
     * @param periodId
     * @param periodType
     * @return
     */
    private Integer deductPeriod(int teacherId, int studentId, int lessonId, Date beginTime, int periodId, int periodType,int studentLessonId, boolean modifyLevelTag,int currentLessonState) {
        int curPeriods = 0, overPeriods = 0, subLevel = 0, validPeriods = 0, realPeriod = 0, extraPeriod = 0, needPeriod = -1;
        int vaildPreviwPeriods = 0;
        int teacherState = 0;
        int studentState = 0;

        // 首先查询当前状态，复原成课程为完成状态，课时数据
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("studentId", studentId);
        paramMap.put("teacherId", teacherId);
        paramMap.put("periodId", periodId);
        int teacherLinkId = 0;
        TeacherCheckin teacherCheckin = teacherCheckinMapper.selectByPeriodAndTeacherId(paramMap);
        if (teacherCheckin != null) {
            teacherState = teacherCheckin.getAbnormalState();
            teacherLinkId = teacherCheckin.getHistId();
            teacherCheckinMapper.deleteByPrimaryKey(teacherLinkId);
        }

        int studentLinkId = 0;
        StudentCheckin studentCheckin = studentCheckinMapper.selectByPeriodAndStudentId(paramMap);
        if (studentCheckin != null) {
            studentState = studentCheckin.getAbnormalState();
            studentLinkId = studentCheckin.getHistId();
            studentCheckinMapper.deleteByPrimaryKey(studentLinkId);
        }

        // 老师和学生都正常上课 学生剩余课时减1,完成课时加1,当前用户等级加1,老师有效课时加1
        // 学生老师信息回退
        if (teacherState == 0 && studentState == 0 && currentLessonState != 5) {
            curPeriods = 1;
            overPeriods = -1;
            subLevel = -1;
            validPeriods = -1;
            realPeriod = -1;
        }

        if (teacherState != 0) {
            if (studentState == 0) {
                // 老师旷课、课程异常、严重迟到+1 学生剩余课时加1,完成课时不变,当前用户等级不变,老师当前课时无效,有效课时减1
                if (teacherState == 3 || teacherState == 7 || teacherState == 8) {
                    curPeriods = -1;
                    vaildPreviwPeriods = modifyLevelTag ? -2 : -1;
                    extraPeriod = -1;
                    // 老师旷迟到小于2分钟 学生剩余课时减1,完成课时加1,当前用户等级加1,老师有效课时加1
                } else if (teacherState == 4) {
                    curPeriods = 1;
                    overPeriods = -1;
                    subLevel = -1;
                    validPeriods = -1;
                    realPeriod = -1;
                    // 老师严重迟到小于15分钟,大于2分钟
                    // 学生剩余课时加1,完成课时加1,当前用户等级加1,老师当前课时无效,有效课时减1
                } else if (teacherState == 5) {
                    overPeriods = -1;
                    subLevel = -1;
                    vaildPreviwPeriods = -1;
                    extraPeriod = -1;
                    realPeriod = -1;
                }
                // 老师状态异常，学生正常，责任归属老师
                if (curPeriods != 0) {
                    // 给学生申请一节课时
                    ApplyPeriods applyPeriods = new ApplyPeriods();
                    applyPeriods.setLinkid(teacherLinkId);
                    applyPeriods.setUserId(studentId);
                    applyPeriods.setUserType(1);
                    applyPeriodsMapper.deleteByUserIdAndLinkId(applyPeriods);
                }
                if (validPeriods != 0) {
                    // 给给老师处罚，增加一节免费课（相当于有效课时减1）
                    ApplyPeriods applyPeriods = new ApplyPeriods();
                    applyPeriods.setLinkid(teacherLinkId);
                    applyPeriods.setUserId(teacherId);
                    applyPeriods.setUserType(2);
                    applyPeriodsMapper.deleteByUserIdAndLinkId(applyPeriods);
                }

                if (teacherState > 2 && teacherState < 9 && teacherState != 7) {
                    CheckinInfo checkinInfo = talkUserService.getCheckinInfo(teacherId);
                    if (checkinInfo != null) {
                        // 旷课
                        if (teacherState == 3) {
                            checkinInfo.setDayOffTimes(Math.max((checkinInfo.getDayOffTimes() == null ? 0 : checkinInfo.getDayOffTimes()) - 1, 0));
                            // 早退
                        } else if (teacherState == 6) {
                            checkinInfo.setLeaveEarlyTimes(Math.max((checkinInfo.getLeaveEarlyTimes() == null ? 0 : checkinInfo.getLeaveEarlyTimes()) - 1, 0));
                            // 迟到
                        } else if (teacherState == 4 || teacherState == 5 || teacherState == 8) {
                            checkinInfo.setDelayTimes(Math.max((checkinInfo.getDelayTimes() == null ? 0 : checkinInfo.getDelayTimes()) - 1, 0));
                        }
                        talkUserService.modifyCheckinInfo(checkinInfo);
                    }
                }
            }
        }

        if (studentState != 0) {
            // 学生旷课、严重迟到、课程异常 学生剩余课时减1,完成课时不变,当前用户等级不变,老师有效课时加1
            if (studentState == 3 || studentState == 5 || studentState == 7) {
                curPeriods = 1;

                validPeriods = -1;
                // 学生迟到小于15分钟 学生剩余课时减1,完成课时加1,当前用户等级加1,老师有效课时加1
            } else if (studentState == 4) {
                curPeriods = 1;
                overPeriods = -1;
                subLevel = -1;

                validPeriods = -1;
                realPeriod = -1;
            }
        }

        if (studentState == 7 || teacherState == 7) {
            StudentLesson studentLesson = new StudentLesson();
            studentLesson.setHistId(studentLessonId);
            studentLesson.setLessonState(2);
            studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
        }
        if (studentState > 2 && studentState < 7) {
            CheckinInfo checkinInfo = talkUserService.getCheckinInfo(studentId);
            if (checkinInfo != null) {
                // 旷课
                if (studentState == 3) {
                    checkinInfo.setDayOffTimes((checkinInfo.getDayOffTimes() == null ? 0 : checkinInfo.getDayOffTimes()) - 1);
                    // 早退
                } else if (studentState == 6) {
                    checkinInfo.setLeaveEarlyTimes((checkinInfo.getLeaveEarlyTimes() == null ? 0 : checkinInfo.getLeaveEarlyTimes()) - 1);
                    // 迟到
                } else if (studentState == 4 || studentState == 5) {
                    checkinInfo.setDelayTimes((checkinInfo.getDelayTimes() == null ? 0 : checkinInfo.getDelayTimes()) - 1);
                }
                talkUserService.modifyCheckinInfo(checkinInfo);
            }
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", studentId);
        if (periodType == TalkCommonEnum.PERIOD_TYPE_DEMO) {
            param.put("periodsType", 3);
        } else {
            param.put("periodsType", periodType);
        }

        // 查询当前学生课时数
        StudentPeriods studentPeriods = studentPeriodsMapper.selectByUserIdAndPeriodType(param);

        // 表示学生老师都迟到，扣除当前学生剩余课时，上完课程不加，学生等级不加，标记当前预约课程完成，老师课时加一
        if (studentPeriods != null) {
            // 取最大当前剩余课时，不能减未负数
            int tempCurPeriods = 0;
            if (studentPeriods.getCurPeriods() != null) {
                tempCurPeriods = studentPeriods.getCurPeriods();
            }
            curPeriods = Math.max(tempCurPeriods + curPeriods, 0);

            int tempOverPeriods = 0;
            if (studentPeriods.getOverPeriods() != null) {
                tempOverPeriods = studentPeriods.getOverPeriods();
            }
            overPeriods = tempOverPeriods + overPeriods;

            // 当前课程数小于0 表示老师课程异常回退
            if (vaildPreviwPeriods < 0) {
                Integer tempVaildPreviwPeriods = studentPeriods.getVaildPreviwPeriods();
                if (tempVaildPreviwPeriods != null && tempVaildPreviwPeriods.intValue() > 0) {
                    studentPeriods.setVaildPreviwPeriods(vaildPreviwPeriods + tempVaildPreviwPeriods);
                }
            }

            // 修改用户等级
            if (subLevel != 0 && modifyLevelTag) {
                // 查询当前完结课程上一个等级
                StudentInfo studentInfo = talkUserService.getStudentInfoByUserId(studentId);
                if (studentInfo != null) {
                    int currentSubLevel = studentInfo.getSubLevel();
                    int userLevel = studentInfo.getUserLevel();
                    if (currentSubLevel + subLevel > 0) {
                        currentSubLevel = currentSubLevel + subLevel;
                    }
                    modifyUserLevel(studentId, userLevel, currentSubLevel);
                }
                // 重新刷新已预约课程
            }
            studentPeriods.setCurPeriods(curPeriods);
            studentPeriods.setOverPeriods(overPeriods);

            studentPeriods.setAccumulativeTotal(25 * 60 * 1000 * overPeriods);

            studentPeriodsMapper.updateByPrimaryKeySelective(studentPeriods);
        }

        updateTeacherPeriod(teacherId, periodType, periodId, needPeriod, validPeriods, realPeriod, extraPeriod, beginTime);
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkOrderService#modifyOverLessonStatus
     * (java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public boolean modifyOverLessonStatus(Integer userId, Integer periodId, Integer lessonState) {
        if (userId != null && periodId != null && lessonState != null) {
            if (lessonState > 1 && lessonState < 10) {

                // 获取用户信息
                UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
                if (userInfo != null) {
                    PublishLesson publishLesson = talkPublishService.getPublishLessonById(periodId);
                    if (publishLesson != null) {
                        if (publishLesson.getPublishType().intValue() == TalkCommonEnum.PUBLISH_TYPE_GENNEL) {
                            int accountType = userInfo.getAccountType();
                            int teacherState = 0, studentState = 0;

                            Map<String, Object> param = new HashMap<String, Object>();
                            if (accountType == TalkCommonEnum.USER_ACCOUNT_TYPE_STUDENT) {
                                // 学生没有严重迟到+1
                                if (lessonState == 8) {
                                    return false;
                                }
                                studentState = lessonState;
                                param.put("studentId", userId);
                            } else if (accountType == TalkCommonEnum.USER_ACCOUNT_TYPE_TEACHER) {
                                teacherState = lessonState;
                                param.put("teacherId", userId);
                            } else {
                                return false;
                            }
                            // 课程正常完成
                            if (lessonState == 2) {
                                teacherState = 0;
                                studentState = 0;
                            }
                            // 查询用户下一节课是否开始，如果开始直接返回异常，防止调整课程出现混乱
                            param.put("periodId", periodId);
                            StudentLesson studentLesson = studentLessonMapper.selectByUserIdAndPeriodId(param);
                            if (studentLesson != null) {
                                int currentLessonState = studentLesson.getLessonState();
                                // 必须是已完结并且不是请假
                                if (currentLessonState > 1 && currentLessonState != 3) {
                                    boolean modifyLevelTag = true;
                                    int consType = studentLesson.getConsType();
                                    
                                    param.put("teacherId", studentLesson.getTeacherId());
                                    param.put("studentId", studentLesson.getStudentId());
                                    
                                    param.put("consType", consType);
                                    param.put("lastBeginTime", publishLesson.getEndTime());
                                    param.put("stateList", Arrays.asList(0, 1, 2, 4, 5));
                                    StudentLesson nextStudentLesson = studentLessonMapper.getNextByUserIdAndType(param);
                                    if (nextStudentLesson != null) {
                                        int nextLessonState = nextStudentLesson.getLessonState();
                                        // 下一条已经进行中或已完结、课程异常 0 已预约 1 进行中 2
                                        // 已完结(迟到、早退、旷课) 3 课程取消（请假） 4 课程异常 5
                                        // 课程异常(非人为)
                                        if (nextLessonState != 0) {
                                            modifyLevelTag = false;
                                        }
                                        hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus modifyLessonState:" + lessonState + ",periodId:"+periodId+",nextLessonState:" + nextLessonState + "nextPeriodId:" + nextStudentLesson.getPeriodId() + ",modifyLevelTag:" + modifyLevelTag);
                                    }

                                    Date beginTime = studentLesson.getBeginTime();
                                    int lessonId = studentLesson.getLessonId();
                                    int teacherId = studentLesson.getTeacherId();
                                    int studentId = studentLesson.getStudentId();

                                    // 调试日志
                                    // 数据回退前，课程状态、学生考勤信息、老师课时信息
                                    StudentInfo studentInfo = talkUserService.getStudentInfoByUserId(studentId);
                                    CheckinInfo studentCheckinInfo = talkUserService.getCheckinInfo(studentId);

                                    int periodType = consType;
                                    if (consType == TalkCommonEnum.PERIOD_TYPE_DEMO) {
                                        periodType = 3;
                                    }
                                    StudentPeriods studentPeriods = talkUserService.getStudentPeriods(studentId, periodType);

                                    // 老师考勤信息
                                    TeacherOverPeriodCount teacherOverPeriodCount = getTeacherOverPeriodCount(teacherId);
                                    CheckinInfo teacherCheckinInfo = talkUserService.getCheckinInfo(teacherId);

                                    param.put("studentId", studentId);
                                    param.put("periodId", periodId);
                                    StudentCheckin studentCheckin = studentCheckinMapper.selectByPeriodAndStudentId(param);
                                    param.put("teacherId", teacherId);
                                    TeacherCheckin teacherCheckin = teacherCheckinMapper.selectByPeriodAndTeacherId(param);

                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before modifyLessonState:" + lessonState);
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before LessonState:" + studentLesson.getLessonState());
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before studentInfo:userId:" + studentInfo.getUserId() + ",userType:" + studentInfo.getUserType() + ",userLevel:" + studentInfo
                                            .getUserLevel() + ",subLevel:" + studentInfo.getSubLevel());
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before studentCheckinInfo:" + new Gson().toJson(studentCheckinInfo));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before studentPeriods:" + new Gson().toJson(studentPeriods));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before teacherOverPeriodCount:" + new Gson().toJson(teacherOverPeriodCount));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before teacherCheckinInfo:" + new Gson().toJson(teacherCheckinInfo));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before studentCheckin:" + new Gson().toJson(studentCheckin));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus before teacherCheckin:" + new Gson().toJson(teacherCheckin));

                                    deductPeriod(teacherId, studentId, lessonId, beginTime, periodId, consType, studentLesson.getHistId(), modifyLevelTag,currentLessonState);

                                    studentInfo = talkUserService.getStudentInfoByUserId(studentId);
                                    studentCheckinInfo = talkUserService.getCheckinInfo(studentId);
                                    studentPeriods = talkUserService.getStudentPeriods(studentId, periodType);

                                    // 老师考勤信息
                                    teacherOverPeriodCount = getTeacherOverPeriodCount(teacherId);
                                    teacherCheckinInfo = talkUserService.getCheckinInfo(teacherId);

                                    studentCheckin = studentCheckinMapper.selectByPeriodAndStudentId(param);
                                    teacherCheckin = teacherCheckinMapper.selectByPeriodAndTeacherId(param);
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after studentInfo:userId:" + studentInfo.getUserId() + ",userType:" + studentInfo
                                            .getUserType() + ",userLevel:" + studentInfo.getUserLevel() + ",subLevel:" + studentInfo.getSubLevel());
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after studentCheckinInfo:" + new Gson().toJson(studentCheckinInfo));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after studentPeriods:" + new Gson().toJson(studentPeriods));

                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after teacherOverPeriodCount:" + new Gson().toJson(teacherOverPeriodCount));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after teacherCheckinInfo:" + new Gson().toJson(teacherCheckinInfo));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after studentCheckin:" + new Gson().toJson(studentCheckin));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus deductPeriod after teacherCheckin:" + new Gson().toJson(teacherCheckin));
                                    if (lessonState != 9) {
                                        // 先把课程异常非人为状态改正常状态
                                        if (studentLesson.getLessonState() == 5) {
                                            studentLesson.setLessonState(2);
                                            studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                                        }
                                        computePeriod(teacherState, teacherId, studentState, studentId, lessonId, beginTime, periodId, consType, studentLesson.getHistId(),modifyLevelTag);
                                    }else{
                                        studentLesson.setLessonState(5);
                                        studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                                    }
                                    studentInfo = talkUserService.getStudentInfoByUserId(studentId);
                                    studentCheckinInfo = talkUserService.getCheckinInfo(studentId);
                                    studentPeriods = talkUserService.getStudentPeriods(studentId, periodType);

                                    // 老师考勤信息
                                    teacherOverPeriodCount = getTeacherOverPeriodCount(teacherId);
                                    teacherCheckinInfo = talkUserService.getCheckinInfo(teacherId);
                                    studentCheckin = studentCheckinMapper.selectByPeriodAndStudentId(param);
                                    teacherCheckin = teacherCheckinMapper.selectByPeriodAndTeacherId(param);

                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after studentInfo:userId:" + studentInfo.getUserId() + ",userType:" + studentInfo
                                            .getUserType() + ",userLevel:" + studentInfo.getUserLevel() + ",subLevel:" + studentInfo.getSubLevel());
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after studentCheckinInfo:" + new Gson().toJson(studentCheckinInfo));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after studentPeriods:" + new Gson().toJson(studentPeriods));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after teacherOverPeriodCount:" + new Gson().toJson(teacherOverPeriodCount));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after teacherCheckinInfo:" + new Gson().toJson(teacherCheckinInfo));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after studentCheckin:" + new Gson().toJson(studentCheckin));
                                    hadoopLogger.info("userId:"+userId+",modifyOverLessonStatus computePeriod after teacherCheckin:" + new Gson().toJson(teacherCheckin));

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String queryUserInfo(int userId) {
        UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
        int accountType = userInfo.getAccountType();
        JSONObject result = new JSONObject();
        // 学生
        if (accountType == 1) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            params.put("periodsType", TalkCommonEnum.PERIOD_TYPE_GENNEL);
            StudentPeriods studentPeriods = studentPeriodsMapper.selectByUserIdAndPeriodType(params);
            StudentInfo student = talkUserService.getStudentInfoByUserId(userInfo.getUserId());
            LessonLevel level = talkLessonService.getLessonLevelById(student.getSubLevel(), student.getUserLevel());
            result.put("身份", "学生");
            result.put("剩余课时", studentPeriods.getCurPeriods());
            result.put("已完成课时", studentPeriods.getOverPeriods());
            result.put("课程等级", level.getLevelName());
            result.put("大等级", level.getParentLevel());

        } else {
            TeacherOverPeriodCount teacherOverPeriodCount = teacherLessonHistMapper.selectSum(userId);
            result.put("身份", "老师");
            result.put("实际课时", teacherOverPeriodCount.getRealPeriods());
            result.put("有效课时", teacherOverPeriodCount.getVaildPeriods());
            result.put("应上课时", teacherOverPeriodCount.getNeedPeriods());
        }
        return result.toJSONString();
    }

    @Override
    public StudentLesson queryStudentLessonByPeriodId(Integer periodId) {
        StudentLesson lesson = null;
        try {
            lesson = studentLessonMapper.getOrderLessonByPeriodId(periodId);
        } catch (Exception e) {
            LOGGER.error("TalkOrderServiceImpl.queryStudentLessonByPeriodId is error "+e.getLocalizedMessage());
        }
        return lesson;
    }

    /**
     * @param studentId
     * @param userLevel
     * @param subLevel
     * @return
     */
    @Override
    public boolean modifyUserLevel(Integer studentId, Integer userLevel, Integer subLevel) {
        if (studentId != null && (userLevel != null || subLevel != null)) {
            // 修改用户等级
            StudentInfo studentInfo = userStudentMapper.selectByPrimaryKey(studentId);
            if (studentInfo != null) {

                try {
                    hadoopLogger.info("userId:" + studentId + ",currentStudentInfo:"+GSON.toJson(studentInfo));
                } catch (Exception e) {
                }
                int userType = studentInfo.getUserType();
                if (userLevel == null) {
                    userLevel = studentInfo.getUserLevel();
                }
                if (subLevel == null) {
                    subLevel = studentInfo.getSubLevel();
                }
                try {
                    hadoopLogger.info("userId:" + studentId + ",userLevel:"+userLevel+",subLevel:"+subLevel);
                } catch (Exception e) {
                }

                // 要修改等级和当前等级相同
                if (studentInfo.getUserLevel().intValue() != userLevel.intValue() || studentInfo.getSubLevel().intValue() != subLevel.intValue()) {
                    studentInfo.setUserId(studentId);
                    studentInfo.setUserLevel(userLevel);
                    studentInfo.setSubLevel(subLevel);
                    userStudentMapper.updateByPrimaryKeySelective(studentInfo);
                }
                // 重新刷新已预约课程
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("studentId", studentId);
                paramMap.put("stateList", Arrays.asList(0));
                int publishType = TalkCommonEnum.PERIOD_TYPE_GENNEL;
                if (userType < 1) {
                    publishType = TalkCommonEnum.PERIOD_TYPE_DEMO;
                }
                paramMap.put("publishType", publishType);
                List<OrderLesson> list = studentLessonMapper.getOrderLesson(paramMap);

                Collections.sort(list, new Comparator<OrderLesson>() {

                    @Override
                    public int compare(OrderLesson o1, OrderLesson o2) {
                        long objTime1 = o1.getBeginTime().getTime();
                        long objTime2 = o2.getBeginTime().getTime();
                        if (objTime2 < objTime1) {
                            return 1;
                        } else if (objTime2 > objTime1) {
                            return -1;
                        }
                        return 0;
                    }
                });

                // 查询当前用户等级对应所有课程子等级
                List<LessonLevel> levelList = talkLessonService.selectByParentLevel(userLevel);
                // 通过userLevel获取对应子等级列表
                if (levelList != null && levelList.size() > 0) {
                    // 通过主键ID进行排序
                    Collections.sort(levelList, new Comparator<LessonLevel>() {

                        @Override
                        public int compare(LessonLevel a, LessonLevel b) {
                            Integer one = a.getLevelId();
                            Integer two = b.getLevelId();
                            return one.compareTo(two);
                        }
                    });
                }

                // 查询对应学生大等级的课程信息列表
                List<Lesson> lessonList = talkLessonService.getLessonList(userLevel, 1, 1, null, null);
                if (lessonList != null && lessonList.size() > 0) {
                    // 课程信息列表转成Map存储Map<lessonLevel,Lesson>
                    Map<Integer, Lesson> map = new HashMap<Integer, Lesson>();
                    for (Lesson tempLesson : lessonList) {
                        map.put(tempLesson.getSubLevel(), tempLesson);
                    }

                    int periodId = 0;

                    StudentLesson studentLesson = null;
                    for (OrderLesson orderLesson : list) {
                        if (orderLesson.getLessonId() == null) {
                            continue;
                        }
                        periodId = orderLesson.getPeriodId();
                        studentId = orderLesson.getStudentId();
                        int consType = orderLesson.getPublishType();

                        int currentOrderLevel = -1;
                        int nextLevel = -1;
                        for (LessonLevel lessonLevel : levelList) {
                            currentOrderLevel = lessonLevel.getLevelId();
                            if (currentOrderLevel > subLevel) {
                                nextLevel = currentOrderLevel;
                                break;
                            }
                        }

                        Lesson lesson = null;
                        // 通过等级获取课程ID
                        if (map.containsKey(nextLevel)) {
                            lesson = map.get(nextLevel);
                        }

                        // 修改标记true表示，后面的预约课程ID顺延
                        if (lesson != null) {
                            int lessonId = lesson.getLessonId();
                            subLevel = lesson.getSubLevel();
                            if (lessonId != orderLesson.getLessonId().intValue()) {
                                studentLesson = new StudentLesson();
                                studentLesson.setHistId(orderLesson.getHistId());
                                studentLesson.setLessonId(lessonId);
                                studentLesson.setLessonUrl(lesson.getLessonUrl());
                                studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                            }
                            try {
                                hadoopLogger.info("userId:" + studentId + ",refresh modifyUserLevel student_lesson hist_id:"+orderLesson.getHistId()+",periodId:"+orderLesson.getPeriodId()+",lessonId:"+lessonId+",beginTime:"+DateUtil.formatDate(orderLesson.getBeginTime(), "yyyy-MM-dd HH:mm:ss")+",lessonLevel:"+lesson.getLessonLevel()+",subLevel:"+lesson.getSubLevel());
                            } catch (Exception e) {
                            }
                        } else {
                            PublishLesson publishLesson = new PublishLesson();
                            publishLesson.setPeriodId(periodId);
                            publishLesson.setState(1);
                            publishLesson.setStudentId(null);
                            publishLesson.setOrderNum(0);
                            int resultCode = publishLessonMapper.updateByPrimaryKeySelective(publishLesson);
                            if (resultCode > 0) {
                                studentLessonMapper.deleteByPrimaryKey(orderLesson.getHistId());

                                int periodType = consType;
                                if (consType == TalkCommonEnum.PERIOD_TYPE_DEMO) {
                                    periodType = 3;
                                }
                                // 删除已预约课程，退回剩余可约课时
                                StudentPeriods studentPeriods = talkUserService.getStudentPeriods(studentId, periodType);
                                if (studentPeriods != null) {
                                    studentPeriods.setVaildPreviwPeriods(Math.max(studentPeriods.getVaildPreviwPeriods() + 1 , 0));
                                    talkUserService.modifyStudentPeriods(studentPeriods);
                                }
                            }
                            try {
                                hadoopLogger.info("userId:" + studentId + ",modifyUserLevel delete student_lesson periodId:"+orderLesson.getPeriodId()+",lessonId:"+orderLesson.getLessonId()+",beginTime:"+DateUtil.formatDate(orderLesson.getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
                            } catch (Exception e) {
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
