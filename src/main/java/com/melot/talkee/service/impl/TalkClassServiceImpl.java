package com.melot.talkee.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.melot.kktv.util.DateUtil;
import com.melot.talkee.dao.StudentLessonMapper;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melot.talkee.dao.ActionLessioningMapper;
import com.melot.talkee.dao.LessioningInoutMapper;
import com.melot.talkee.dao.WhiteboardMapper;

/**
 * Created by mn on 2017/5/9.
 */
@Service
public class TalkClassServiceImpl implements TalkClassService {

    private static Logger logger = Logger.getLogger(TalkClassServiceImpl.class);
    @Autowired
    private TalkUserService talkUserService;
    @Autowired
    private ActionLessioningMapper actionLessioningMapper;
    @Autowired
    private TalkPublishService talkPublishService;
    @Autowired
    private TalkOrderService talkOrderService;

    @Autowired
    private TalkRecordService talkRecordService;

    @Autowired
    private LessioningInoutMapper lessioningInoutMapper;

    @Autowired
    private WhiteboardMapper whiteboardMapper;

    @Autowired
    private StudentLessonMapper studentLessonMapper;

    @Override
    @Transactional
    public String enterClass(Integer userId, Integer roleType, Integer periodId, Integer platform, String deviceUid) {
        String tagCode = TalkTagCodeEnum.SUCCESS;
        Date eventTime = new Date();
        // 进出流水
        LessioningInout lessioningInout = new LessioningInout();
        // 设置通用参数
        lessioningInout.setPeriodId(periodId);
        lessioningInout.setPlatform(platform);
        lessioningInout.setDeviceUid(deviceUid);
        lessioningInout.setUserId(userId);
        lessioningInout.setInTime(eventTime);
        lessioningInout.setRoleType(roleType);
        lessioningInoutMapper.insertSelective(lessioningInout);
        //CC,CR,或者家长进入返回
        if (roleType != 1) {
            return tagCode;
        }
        if (userId != null && periodId != null && platform != null) {
            UserInfo user = talkUserService.getUserInfoByUserId(userId);
            if (user == null) {
                logger.info("talkClassService.enterClass. userId:[" + userId + "] is null");
                return TalkTagCodeEnum.USER_NOT_EXIST;
            }
            PublishLesson lesson = talkPublishService.getPublishLessonById(periodId);
            if (lesson == null) {
                logger.info("talkClassService.enterClass. periodId:[" + periodId + "] is null");
                return TalkTagCodeEnum.LESSON_NOT_EXIST;
            }

            int userType = user.getAccountType();
            // 普通课、调试课 、公开课
            List<OrderLesson> orderLessonList = talkOrderService.getOrderLessonByPeriodList(Arrays.asList(periodId));
            if (orderLessonList != null && orderLessonList.size() > 0) {

                // 获取老师的进出时间
                Map<String, Object> params = null;
                // 判断记录是否存在
                ActionLessioning actionLessioning = null;
                for (OrderLesson orderLesson : orderLessonList) {
                    talkRecordService.startRecord(String.valueOf(periodId), orderLesson.getEndTime());
                    int studentId = orderLesson.getStudentId();
                    int teacherId = orderLesson.getTeacherId();
                    params = new HashMap<String, Object>();
                    // 学生进入
                    if (userType == 1) {
                        if (studentId != userId.intValue()) {
                            continue;
                        }
                        params.put("teacherId", teacherId);
                        // 老师进入
                    } else if (userType == 2) {
                        if (teacherId != userId.intValue()) {
                            continue;
                        }
                    }
                    params.put("periodId", periodId);
                    params.put("studentId", studentId);
                    actionLessioning = actionLessioningMapper.selectFirstByPeriodUser(params);
                    if (actionLessioning != null) {
                        // 学生进入
                        if (userType == 1) {
                            if (actionLessioning.getStudentInTime() == null) {
                                actionLessioning.setStudentInTime(eventTime);
                            }
                            // 老师进入
                        } else if (userType == 2) {
                            if (actionLessioning.getTeacherInTime() == null) {
                                actionLessioning.setTeacherInTime(eventTime);
                            }
                        }
                        actionLessioningMapper.updateByPrimaryKey(actionLessioning);
                    } else {
                        // 初始化
                        actionLessioning = new ActionLessioning();
                        // 学生进入
                        if (userType == 1) {
                            actionLessioning.setStudentInTime(eventTime);
                            // 老师进入
                        } else if (userType == 2) {
                            actionLessioning.setTeacherInTime(eventTime);
                        }
                        actionLessioning.setStudentId(studentId);
                        actionLessioning.setPeriodId(periodId);
                        actionLessioning.setBeginTime(lesson.getBeginTime());
                        actionLessioning.setEndTime(lesson.getEndTime());
                        actionLessioning.setTeacherId(teacherId);
                        actionLessioning.setState(0);

                        actionLessioningMapper.insert(actionLessioning);
                    }
                }
            }

        } else {
            tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
            logger.info("talkClassService.enterClass. userId:[" + userId + "] or periodId:[" + periodId + "] or platform:[" + platform + "] is null");
        }
        return tagCode;
    }

    @Override
    @Transactional
    public String outClass(Integer userId, Integer roleType, Integer periodId, Integer platform, String deviceUid) {
        String tagCode = TalkTagCodeEnum.SUCCESS;
        Date eventTime = new Date();
        Map<String, Object> params = new HashMap<>();
        try {
            params.clear();
            params.put("periodId", periodId);
            params.put("platForm", platform);
            params.put("UserId", userId);
            params.put("deviceUid", deviceUid);
            params.put("roleType", roleType);
            // 进出流水
            LessioningInout lessioningInout = lessioningInoutMapper.selectLastestByPeriodAndUserId(params);
            if (lessioningInout != null) {
                lessioningInout.setOutTime(eventTime);
                lessioningInoutMapper.updateByPrimaryKey(lessioningInout);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" sql exception");
        }

        if (roleType != 1) {
            return tagCode;
        }
        if (userId != null && periodId != null && platform != null) {
            UserInfo user = talkUserService.getUserInfoByUserId(userId);
            if (user == null) {
                logger.info("talkClassService.enterClass. userId:[" + userId + "] is null");
                return TalkTagCodeEnum.USER_NOT_EXIST;
            }

            PublishLesson lesson = talkPublishService.getPublishLessonById(periodId);
            if (lesson == null) {
                logger.info("talkClassService.enterClass. periodId:[" + periodId + "] is null");
                return TalkTagCodeEnum.LESSON_NOT_EXIST;
            }


            int userType = user.getAccountType();
            // 普通课、调试课 、公开课
            List<OrderLesson> orderLessonList = talkOrderService.getOrderLessonByPeriodList(Arrays.asList(periodId));
            if (orderLessonList != null && orderLessonList.size() > 0) {
                // 获取老师的进出时间

                // 判断记录是否存在
                ActionLessioning actionLessioning = null;
                for (OrderLesson orderLesson : orderLessonList) {
                    int studentId = orderLesson.getStudentId();
                    int teacherId = orderLesson.getTeacherId();
                    // 学生进入
                    params = new HashMap<String, Object>();
                    if (userType == 1) {
                        if (studentId != userId.intValue()) {
                            continue;
                        }
                        params.put("teacherId", teacherId);
                        // 老师进入
                    } else if (userType == 2) {
                        if (teacherId != userId.intValue()) {
                            continue;
                        }
                    }
                    params.put("periodId", periodId);
                    params.put("studentId", studentId);
                    actionLessioning = actionLessioningMapper.selectFirstByPeriodUser(params);
                    if (actionLessioning != null) {
                        // 学生出去
                        if (userType == 1) {
                            actionLessioning.setStudentOutTime(eventTime);
                            // 老师出去
                        } else if (userType == 2) {
                            actionLessioning.setTeacherOutTime(eventTime);
                        }
                        actionLessioningMapper.updateByPrimaryKey(actionLessioning);
                    } else {
                        // 初始化
                        actionLessioning = new ActionLessioning();
                        // 学生出去
                        if (userType == 1) {
                            actionLessioning.setStudentOutTime(eventTime);
                            // 老师出去
                        } else if (userType == 2) {
                            actionLessioning.setTeacherOutTime(eventTime);
                        }
                        actionLessioning.setStudentId(studentId);
                        actionLessioning.setPeriodId(periodId);
                        actionLessioning.setBeginTime(lesson.getBeginTime());
                        actionLessioning.setEndTime(lesson.getEndTime());
                        actionLessioning.setTeacherId(teacherId);
                        actionLessioning.setState(0);
                        actionLessioningMapper.insert(actionLessioning);
                    }
                }


            }
        } else {
            tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
            logger.info("talkClassService.enterClass. userId:[" + userId + "] or periodId:[" + periodId + "] or platform:[" + platform + "] is null");
        }
        return tagCode;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.melot.talkee.driver.service.TalkClassService#recordWhiteboard(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @Override
    public String recordWhiteboard(Integer userId, Integer periodId, Integer segment, String msgData) {
        if (periodId != null && segment != null && StringUtils.isNotBlank(msgData)) {
           /* UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
            if (userInfo == null) {
                logger.info("talkClassService.enterClass. userId:[" + userId + "] is null");
                return TalkTagCodeEnum.USER_NOT_EXIST;
            }
            if (userInfo.getAccountType() == 1) {
                logger.info("talkClassService.enterClass. userId:[" + userId + "] unauthorized");
                return TalkTagCodeEnum.UNAUTHORIZED;
            }*/
            PublishLesson lesson = talkPublishService.getPublishLessonById(periodId);
            if (lesson == null) {
                logger.info("talkClassService.enterClass. periodId:[" + periodId + "] is null");
                return TalkTagCodeEnum.LESSON_NOT_EXIST;
            }
            Whiteboard whiteboard = new Whiteboard(userId, periodId, segment, msgData);

            whiteboardMapper.insertSelective(whiteboard);

            return TalkTagCodeEnum.SUCCESS;
        } else {
            logger.info("talkClassService.recordWhiteboard. userId:[" + userId + "] or periodId:[" + periodId + "] or segment:[" + segment + "] or msgData:[" + msgData + "] is null");
            return TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.melot.talkee.driver.service.TalkClassService#getWhiteboardList(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Whiteboard> getWhiteboardList(Integer userId, Integer periodId, Integer segment) {
        if (userId != null && periodId != null && segment != null) {
            Whiteboard whiteboard = new Whiteboard(userId, periodId, segment);
            return whiteboardMapper.selectBySelective(whiteboard);
        }
        return null;
    }


    @Override
    public Whiteboard getWhiteBoard(Integer periodId, Integer segment, Integer offset, Integer limit) {
        if (periodId != null && segment != null && offset != null && limit != null) {
            HashMap<String, Object> params = new HashMap();
            params.put("periodId", periodId);
            params.put("segment", segment);
            params.put("limit", limit);
            params.put("offset", offset);

            return whiteboardMapper.selectByParams(params);
        }
        return null;
    }

    @Override
    public LessonRecord getLessonRecord(Integer periodId) {
        HashMap<String, Object> lesson = studentLessonMapper.selectLessonRecordByPeriodId(periodId);
        if (lesson != null) {
            LessonRecord lessonRecord = new LessonRecord();
            VideoRecording studentVideo = new VideoRecording();
            VideoRecording teacherVideo = new VideoRecording();
            lessonRecord.setLessonId((Integer) lesson.get("lessonId"));
            lessonRecord.setLessonName((String) lesson.get("lessonName"));
            lessonRecord.setPeriodId((Integer) lesson.get("periodId"));

            //deal
            long studentStartTime = 0, teacherStartTime = 0;
            if (lesson.get("studentStartTime") != null) {
                studentStartTime = DateUtil.parseDateTimeStringToDate((String) lesson.get("studentStartTime"), "yyyyMMddHHmmssSSS").getTime();
            }
            if (lesson.get("teacherStartTime") != null) {
                teacherStartTime = DateUtil.parseDateTimeStringToDate((String) lesson.get("teacherStartTime"), "yyyyMMddHHmmssSSS").getTime();
            }
            if (studentStartTime != 0 && teacherStartTime != 0) {
                if (studentStartTime >= teacherStartTime) {
                    studentVideo.setStartTime((int) (studentStartTime - teacherStartTime));
                    teacherVideo.setStartTime(0);
                } else {
                    teacherVideo.setStartTime((int) (teacherStartTime - studentStartTime));
                    studentVideo.setStartTime(0);
                }
            } else {
                teacherVideo.setStartTime(0);
                studentVideo.setStartTime(0);
            }
            studentVideo.setAccountType(1);
            teacherVideo.setAccountType(2);

            if (lesson.get("teacherDuration") != null) {
                teacherVideo.setDuration((Integer) lesson.get("teacherDuration"));
                teacherVideo.setVideoUrl((String) lesson.get("teacherUrl"));
            } else {
                teacherVideo.setDuration(0);
                teacherVideo.setVideoUrl("");
            }

            if (lesson.get("studentDuration") != null) {
                studentVideo.setDuration((Integer) lesson.get("studentDuration"));
                studentVideo.setVideoUrl((String) lesson.get("studentUrl"));
            } else {
                studentVideo.setDuration(0);
                studentVideo.setVideoUrl("");
            }
            teacherVideo.setUserId((Integer) lesson.get("teacherId"));
            studentVideo.setUserId((Integer) lesson.get("studentId"));


            lessonRecord.setStudentVideo(studentVideo);
            lessonRecord.setTeacherVideo(teacherVideo);
            return lessonRecord;
        }
        return null;
    }

    @Override
    public Integer deleteOldRecord(Integer periodId) {
        if (periodId != null) {
            try {
                Integer result = whiteboardMapper.deletebyPeriodId(periodId);
                return result;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return 0;
    }

}
