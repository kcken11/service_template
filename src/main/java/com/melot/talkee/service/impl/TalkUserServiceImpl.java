package com.melot.talkee.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.melot.module.iprepository.driver.domain.IpInfo;
import com.melot.module.iprepository.driver.service.IpRepositoryService;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.cache.CacheService;
import com.melot.talkee.dao.*;
import com.melot.talkee.dao.domain.UserLog;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.service.TalkLessonService;
import com.melot.talkee.driver.service.TalkOrderService;
import com.melot.talkee.driver.service.TalkUserService;
import com.melot.talkee.driver.utils.PasswordFunctions;
import com.melot.talkee.redis.UserRedisSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TalkUserServiceImpl implements TalkUserService {

    private Logger logger = Logger.getLogger(TalkUserServiceImpl.class);

    private static Logger hadoopLogger = Logger.getLogger("hadoopLogger");
    @Autowired
    private UserRegisterMapper userRegisterMapper;

    @Autowired
    private UserStudentMapper userStudentMapper;

    @Autowired
    private UserTeacherMapper userTeacherMapper;

    @Autowired
    private UserResourceMapper userResourceMapper;

    @Autowired
    private PortraitMapper portraitMapper;

    @Autowired
    private UserLogMapper userLogMapper;

    @Autowired
    private StudentPeriodsMapper studentPeriodsMapper;

    @Autowired
    private CheckinInfoMapper checkinInfoMapper;

    @Autowired
    private TalkLessonService talkLessonService;

    @Autowired
    private AdminCityMapper adminCityMapper;

    @Autowired
    private AdminInfoMapper adminInfoMapper;

    @Autowired
    private BuyPeriodsMapper buyPeriodsMapper;

    @Autowired
    private OrderPackageMapper orderPackageMapper;

    @Autowired
    private TeacherLessonMapper teacherLessonMapper;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private TalkOrderService talkOrderService;

    public List<UserInfo> getUserRegisters(UserInfo userRegister) {
        return userRegisterMapper.getUserRegister(userRegister);
    }

    @Override
    public UserInfo getUserInfoByPhoneNumber(String phoneNum) {
        if (StringUtils.isNotBlank(phoneNum)) {
            return userRegisterMapper.getUserRegisterByPhoneNum(phoneNum);
        }
        return null;
    }

    /**
     * 根据用户名称获取用户注册信息
     *
     * @param loginName
     * @return
     */
    @Override
    public UserInfo getUserInfoByLoginName(String loginName) {
        if (StringUtils.isNotBlank(loginName)) {
            return userRegisterMapper.getUserRegisterByLoginName(loginName);
        }
        return null;
    }

    /**
     * 根据email获取用户注册信息
     *
     * @param email
     * @return
     */
    @Override
    public UserInfo getUserInfoByEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            return userRegisterMapper.getUserRegisterByEmail(email);
        }
        return null;
    }

    /**
     * 获取头像信息
     *
     * @return
     */
    @Override
    public String getPortraitByUserId(Integer userId) {

        if (userId != null) {
            String portraitUrl = UserRedisSource.getUserPortraitCache(userId);
            if (StringUtils.isBlank(portraitUrl)) {
                UserResource userResource = userResourceMapper.selectPortraitByUserId(userId);
                if (userResource != null) {
                    Portrait portrait = portraitMapper.selectByPrimaryKey(userResource.getResValue());
                    if (portrait != null) {
                        portraitUrl = portrait.getPortraitUrl();
                        String yunUrl = "";
                        if(StringUtils.isNotBlank(portraitUrl) && portraitUrl.startsWith("aliyun")){
                            yunUrl = MelotBeanFactory.getBean("aliyun", String.class);
                            portraitUrl = yunUrl+ "/" + portraitUrl;
                        }
                    }
                }
                UserRedisSource.setUserPortraitCache(userId, StringUtils.isBlank(portraitUrl) ? "-1" : portraitUrl);
            }
            if (StringUtils.isNotBlank(portraitUrl)) {
                return portraitUrl.equals("-1") ? null : portraitUrl;
            }
        }
        return null;
    }

    /**
     * 获取头像信息
     *
     * @return
     */
    @Override
    public String getPortraitByUserIdAndType(Integer userId, Integer type) {

        if (userId != null) {
            String portraitUrl = UserRedisSource.getUserPortraitCache(userId);
            if (StringUtils.isBlank(portraitUrl)) {
                UserResource userResource = userResourceMapper.getPortraitByUserIdAndType(userId);
                if (userResource != null) {
                    Portrait portrait = portraitMapper.selectByPrimaryKey(userResource.getResValue());
                    if (portrait != null) {
                        portraitUrl = portrait.getPortraitUrl();
                        String yunUrl = "";
                        if(StringUtils.isNotBlank(portraitUrl) && portraitUrl.startsWith("aliyun")){
                            yunUrl = MelotBeanFactory.getBean("aliyun", String.class);
                            portraitUrl = yunUrl+ "/" + portraitUrl;
                        }
                    }
                }
                UserRedisSource.setUserPortraitCache(userId, StringUtils.isBlank(portraitUrl) ? "-1" : portraitUrl);
            }
            if (StringUtils.isNotBlank(portraitUrl)) {
                return portraitUrl.equals("-1") ? null : portraitUrl;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.melot.talkee.service.UserService#registerViaPhoneNum(int, int,
     * int, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    @Transactional
    public Integer registerViaPhoneNum(Integer channel, Integer recommended, Integer platform, Integer port, String phoneNum, String deviceUId, String version, String clientIp, String password) {
        try {
            if (StringUtils.isBlank(phoneNum) && StringUtils.isBlank(password)) {
                logger.error("UserService.registerViaPhoneNum phoneNum and password is null ");
                return null;
            }
            // 现有手机注册accountType 1 学生
            int accountType = 1;
            int channelType = 1;

            UserInfo userInfo = getUserInfoByPhoneNumber(phoneNum);
            if (userInfo != null) {
                logger.error("UserService.registerViaPhoneNum phoneNum has exist ");
                return null;
            }

            Integer cityId = null;
            try {
                if (StringUtils.isNotBlank(clientIp)) {
                    IpRepositoryService ipRepositoryService = MelotBeanFactory.getBean("ipRepositoryService", IpRepositoryService.class);
                    IpInfo ipInfo = ipRepositoryService.getIpInfo(clientIp);
                    if (ipInfo != null) {
                        String city = ipInfo.getCity();
                        if (StringUtils.isNotBlank(city)) {
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("name", city);
                            param.put("cityType", 3);
                            AdminCity adminCity = adminCityMapper.selectByNameAndType(param);
                            if (adminCity != null) {
                                cityId = adminCity.getId();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("");
            }
            // TODO 此处通过查询CRM或默认CCID
            Integer ccId = 288;

            UserInfo userRegister = new UserInfo();
            userRegister.setAccountType(accountType);
            userRegister.setChannelId(channel);
            userRegister.setChannelType(channelType);
            userRegister.setPhoneNum(phoneNum);
            userRegister.setLoginName(phoneNum);
            userRegister.setPort(port);
            userRegister.setRegisterIp(clientIp);
            userRegister.setRegisterTime(new Date());
            userRegister.setTerminal(platform);
            userRegister.setPassword(password);
            if (userRegisterMapper.insertUserRegister(userRegister) > 0) {
                int userId = userRegister.getUserId();
                // 修改用户密码
                modifyPassword(userId, password);
                initStudentInfo(userId, -1, 0, -1, ccId, phoneNum);
                initStudentPeriods(userId, 1, 3);
                return userId;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error("UserService.registerViaPhoneNum is error ：" + e);
        }
        return null;
    }

    /**
     * 初始化学生信息（默认为试听用户，等级未0）
     *
     * @param userId
     * @param userType  0 试听用户 1 普通用户
     * @param userLevel 用户等级 默认1
     * @param subLevel  子等级 默认0
     * @return
     */
    public int initStudentInfo(int userId, int userType, int userLevel, int subLevel, int ccId, String phoneNum) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUserId(userId);
        studentInfo.setUserLevel(userLevel);
        studentInfo.setCcId(ccId);
        studentInfo.setSubLevel(subLevel);
        studentInfo.setUserType(userType);
        studentInfo.setTag(0);
        studentInfo.setPhoneNum(phoneNum);
        studentInfo.setUpdateTime(new Date());
        studentInfo.setGender(-1);
        return userStudentMapper.insertSelective(studentInfo);
    }

    /**
     * 初始化用户课程信息(默认试听用户，剩余课时、所有课时为1)
     *
     * @param userId
     * @param curPeriods  剩余课时数
     * @param periodsType 课时类型 默认 3(试听用户)
     * @return
     */
    public int initStudentPeriods(int userId, int curPeriods, int periodsType) {
        StudentPeriods studentPeriods = new StudentPeriods();
        studentPeriods.setAllPeriods(curPeriods);
        studentPeriods.setCurPeriods(curPeriods);
        studentPeriods.setOverPeriods(0);
        studentPeriods.setDtime(new Date());
        studentPeriods.setUserId(userId);
        studentPeriods.setPeriodsType(periodsType);
        studentPeriods.setVaildPreviwPeriods(curPeriods);
        studentPeriods.setAccumulativeTotal(0);

        return studentPeriodsMapper.insert(studentPeriods);
    }

    /**
     * 初始化用户考勤信息
     *
     * @param userId
     * @param userType 用户类型(1:学生 2：老师)
     * @return
     */
    public int initCheckinInfo(int userId, int userType) {
        CheckinInfo checkinInfo = new CheckinInfo();
        checkinInfo.setAllFreeAskForLeave(0);
        checkinInfo.setDayOffTimes(0);
        checkinInfo.setDelayTimes(0);
        checkinInfo.setFreeAskForLeave(0);
        checkinInfo.setLeaveEarlyTimes(0);
        checkinInfo.setNormalOver(0);
        checkinInfo.setPeriodsAskForLeave(0);
        checkinInfo.setUserId(userId);
        checkinInfo.setUserType(userType);
        return checkinInfoMapper.insert(checkinInfo);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.service.UserService#modifyLoginName(java.lang.Integer
     * ,java.lang.String)
     */
    @Override
    public Integer modifyLoginName(Integer userId, String loginName) {
        if (userId != null && StringUtils.isNotBlank(loginName)) {
            // 检测loginName是否存在
            UserInfo userInfo = getUserInfoByLoginName(loginName);
            if (userInfo != null) {
                // 查询userId和要修改userId不相等
                if (userInfo.getUserId().intValue() != userId.intValue()) {
                    throw new RuntimeException(loginName + " has exist");
                }
            } else {
                userInfo = getUserInfoByUserId(userId);
                if (userInfo != null) {
                    userInfo.setLoginName(loginName);
                    int resultCode = userRegisterMapper.updateUserRegisterById(userInfo);
                    if (resultCode > 0) {
                        UserRedisSource.delUserInfoCache(userId);
                    }
                    return resultCode;
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.service.UserService#getUserInfoByUserId(java.lang.Integer
     * )
     */
    @Override
    public UserInfo getUserInfoByUserId(Integer userId) {
        UserInfo userInfo = null;
        if (userId != null) {
            userInfo = UserRedisSource.getUserInfoCache(userId);
            if (userInfo == null) {
                userInfo = getDbUserInfoByUserId(userId);
            }
        }
        return userInfo;
    }

    public UserInfo getDbUserInfoByUserId(Integer userId) {
        UserInfo userInfo = null;
        if (userId != null) {
            userInfo = userRegisterMapper.getUserRegisterById(userId);
            if (userInfo != null) {
                UserRedisSource.setUserInfoCache(userId, userInfo);
            }
        }
        return userInfo;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getStudentInfoByUserId
     * (java.lang.Integer)
     */
    @Override
    public StudentInfo getStudentInfoByUserId(Integer studentId) {
        StudentInfo studentInfo = null;
        if (studentId != null) {
            studentInfo = UserRedisSource.getStudentInfoCache(studentId);
            if (studentInfo == null) {
                studentInfo = getDbStudentInfoByUserId(studentId);
            }
        }
        return studentInfo;
    }

    public StudentInfo getDbStudentInfoByUserId(Integer studentId) {
        StudentInfo studentInfo = null;
        if (studentId != null) {
            studentInfo = userStudentMapper.selectByPrimaryKey(studentId);
            if (studentInfo != null) {
                UserRedisSource.setStudentInfoCache(studentId, studentInfo);
            }
        }
        return studentInfo;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getTeacherInfoByUserId
     * (java.lang.Integer)
     */
    @Override
    public TeacherInfo getTeacherInfoByUserId(Integer teacherId) {
        TeacherInfo teacherInfo = null;
        if (teacherId != null) {
            teacherInfo = UserRedisSource.getTeacherInfoCache(teacherId);
            if (teacherInfo == null) {
                teacherInfo = getDbTeacherInfoByUserId(teacherId);
            }
        }
        return teacherInfo;
    }

    public TeacherInfo getDbTeacherInfoByUserId(Integer teacherId) {
        TeacherInfo teacherInfo = null;
        if (teacherId != null) {
            teacherInfo = userTeacherMapper.selectByPrimaryKey(teacherId);
            if (teacherInfo != null) {
                UserRedisSource.setTeacherInfoCache(teacherId, teacherInfo);
            }
        }
        return teacherInfo;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.service.UserService#modifyStudentInfo(com.melot.talkee
     * .dao.domain.UserStudent)
     */
    @Override
    @Transactional
    public Integer modifyStudentInfo(String loginName, StudentInfo studentInfo) {

        // 验证对象和userId是否为空
        if (studentInfo != null && studentInfo.getUserId() != null) {
            hadoopLogger.info("modifyStudentInfo --> userId:"+studentInfo.getUserId()+", studentInfo:"+new Gson().toJson(studentInfo));
            if(StringUtils.isNotBlank(studentInfo.getPhoneNum())){
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("phoneNum", studentInfo.getPhoneNum());
                paramMap.put("userId", studentInfo.getUserId());
                List<StudentInfo> infoList = userStudentMapper.selectByPhoneNum(paramMap);
                if (infoList != null && !infoList.isEmpty()) {
                    return null;
                }
            }
            
            Integer userLevel = studentInfo.getUserLevel();
            Integer subLevel = studentInfo.getSubLevel();
            // 修改用户等级，同步修改用户已约课程
            if ((userLevel != null && userLevel > 0) || subLevel != null) {
                talkOrderService.modifyUserLevel(studentInfo.getUserId(), userLevel, subLevel);
            }
            // 更新注册表
            studentInfo.setUpdateTime(new Date());
            int resultCode = userStudentMapper.updateByPrimaryKeySelective(studentInfo);
            if (resultCode > 0) {
                if (StringUtils.isNotBlank(loginName)) {
                    resultCode = modifyLoginName(studentInfo.getUserId(), loginName);
                }
                UserRedisSource.delStudentInfoCache(studentInfo.getUserId());
                return resultCode;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.service.UserService#modifyTeacherInfo(com.melot.talkee
     * .dao.domain.TeacherInfo)
     */
    @Override
    @Transactional
    public Integer modifyTeacherInfo(String loginName, TeacherInfo teacherInfo) {
        // 验证对象和userId是否为空
        if (teacherInfo != null && teacherInfo.getTeacherId() != null) {
            int resultCode = userTeacherMapper.updateByPrimaryKeySelective(teacherInfo);
            if (resultCode > 0) {
                updateTeacherLessonLevel(teacherInfo.getTeacherId(), teacherInfo.getCanteacherLevels());
                if (StringUtils.isNotBlank(loginName)) {
                    resultCode = modifyLoginName(teacherInfo.getTeacherId(), loginName);
                }
                UserRedisSource.delTeacherInfoCache(teacherInfo.getTeacherId());
                return resultCode;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.melot.talkee.service.UserService#changePassword(int,
     * java.lang.String)
     */
    @Override
    public boolean modifyPassword(Integer userId, String password) {
        if (userId != null && StringUtils.isNotBlank(password)) {
            try {
                JsonObject udpdJson = new JsonObject();
                udpdJson.addProperty("userId", userId);
                udpdJson.addProperty("password", password);
                String pwd = PasswordFunctions.encryptUDPD(udpdJson);
                UserInfo userRegister = new UserInfo();
                userRegister.setUserId(userId);
                userRegister.setPassword(pwd);
                int code = userRegisterMapper.updateUserRegisterById(userRegister);
                if (code > 0) {
                    // 重新查询DB数据缓存到redis
                    UserRedisSource.delUserInfoCache(userId);
                    return true;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 上传头像信息
     * @param userId
     * @param accountType
     * @param fileUrl
     */
    private void uploadPortrait(int userId,int accountType,String fileUrl){
        Portrait portrait = new Portrait();
        portrait.setPortraitUrl(fileUrl);
        portrait.setStatus(0);
        portrait.setUploadTime(new Date());
        portrait.setUserId(userId);
        int code = portraitMapper.insertSelective(portrait);
        if (code > 0) {
            UserResource userResource = userResourceMapper.selectPortraitByUserId(userId);
            if (userResource != null) {
                userResource.setResValue(portrait.getPortraitId());
                userResource.setRelId(userResource.getRelId());
                userResourceMapper.updateByPrimaryKeySelective(userResource);
            } else {
                userResource = new UserResource();
                userResource.setUserId(userId);
                userResource.setResType(accountType);
                userResource.setResId(1);
                userResource.setResValue(portrait.getPortraitId());
                userResourceMapper.insertSelective(userResource);
            }
            UserRedisSource.delUserPortraitCache(userId);
        }
    }
    
    @Override
    @Transactional
    public boolean fileUpload(Integer userId, Integer fileType, Map<String, String> files, Map<String, Object> paramMap) {
        if (userId != null && fileType != null && files != null && files.size() > 0) {
            UserInfo userInfo = getUserInfoByUserId(userId);
            if (userInfo == null) {
                return false;
            }
            if (fileType == 1) {
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    String filePath = entry.getValue();
                    uploadPortrait(userId,userInfo.getAccountType(),filePath);
                }
            } else if (fileType == 2) {
                if (paramMap.containsKey("lessonId")) {
                    Integer lessonId = (Integer) paramMap.get("lessonId");
                    talkLessonService.uploadCourseware(lessonId, files, userId);
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean adminFileUpload(Integer userId, Integer fileType, Map<String, String> files, Map<String, Object> paramMap) {
        if (userId != null && fileType != null && files != null && files.size() > 0) {
            /*UserInfo userInfo = getUserInfoByUserId(userId);
            if (userInfo == null) {
                return false;
            }*/
            if (fileType == 1) {
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    String filePath = entry.getValue();
                    uploadPortrait(userId,3,filePath);
                }
            } else if (fileType == 2) {
                if (paramMap.containsKey("lessonId")) {
                    Integer lessonId = (Integer) paramMap.get("lessonId");
                    talkLessonService.uploadCourseware(lessonId, files, userId);
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.UserService#recordLogin(java.lang.Integer
     * , java.lang.Integer, java.lang.Integer)
     */
    @Override
    public String recordLogin(Integer userId, Integer loginType, Integer platform, String loginIp) {
        try {

            UserInfo userInfo = getUserInfoByUserId(userId);
            if (userInfo != null) {
                int accountType = userInfo.getAccountType();

                String token = UUID.randomUUID().toString().replaceAll("-", "");
                UserLog userLog = new UserLog();
                userLog.setLoginTime(new Date());
                userLog.setUserId(userId);
                userLog.setLoginType(loginType);
                userLog.setPlatform(platform);
                userLog.setToken(token);
                userLog.setLoginIp(loginIp);
                if (userLogMapper.insertSelective(userLog) > 0) {
                    if (accountType == 1) {
                        StudentInfo studentInfo = new StudentInfo();
                        studentInfo.setUserId(userId);
                        studentInfo.setLastLoginIp(loginIp);
                        studentInfo.setLastLoginTime(userLog.getLoginTime());
                        userStudentMapper.updateByPrimaryKeySelective(studentInfo);
                    } else if (accountType == 2) {
                        TeacherInfo teacherInfo = new TeacherInfo();
                        teacherInfo.setTeacherId(userId);
                        teacherInfo.setLastLoginIp(loginIp);
                        teacherInfo.setLastLoginTime(userLog.getLoginTime());
                        userTeacherMapper.updateByPrimaryKeySelective(teacherInfo);
                    }
                    UserRedisSource.setUserToken(userId, token, platform, 0);
                }
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UserService.recordLogin is error ：" + e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.UserService#recordLogout(java.lang.Integer
     * , java.lang.String)
     */
    @Override
    public void recordLogout(Integer userId, String token, Integer platform) {
        if (userId != null && StringUtils.isNotBlank(token)) {
            UserLog userLog = new UserLog();
            userLog.setUserId(userId);
            userLog.setToken(token);
            UserLog lastLog = userLogMapper.getlastBySelective(userLog);
            if (lastLog != null && lastLog.getLogoutTime() == null) {
                lastLog.setLogoutTime(new Date());
                userLogMapper.updateByPrimaryKeySelective(lastLog);
            }

            UserRedisSource.delUserToken(userId, platform);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getStudentPeriods(java
     * .lang.Integer)
     */
    @Override
    public StudentPeriods getStudentPeriods(Integer userId, Integer periodType) {
        if (userId != null) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userId", userId);
            if (periodType == null || periodType.intValue() > 3) {
                periodType = 0;
            }
            paramMap.put("periodsType", periodType);
            return studentPeriodsMapper.selectByUserIdAndPeriodType(paramMap);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getCheckinInfo(java.lang
     * .Integer)
     */
    @Override
    public CheckinInfo getCheckinInfo(Integer userId) {
        if (userId != null) {
            return checkinInfoMapper.selectByPrimaryKey(userId);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#modifyStudentPeriods(
     * com.melot.talkee.driver.domain.StudentPeriods)
     */
    @Override
    public int modifyStudentPeriods(StudentPeriods studentPeriods) {
        if (studentPeriods != null && studentPeriods.getResId() != null) {
            studentPeriods.setAccumulativeTotal(25 * 60 * 1000 * (studentPeriods.getOverPeriods()));

            return studentPeriodsMapper.updateByPrimaryKeySelective(studentPeriods);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#modifyCheckinInfo(com
     * .melot.talkee.driver.domain.CheckinInfo)
     */
    @Override
    public int modifyCheckinInfo(CheckinInfo checkinInfo) {
        if (checkinInfo != null && checkinInfo.getUserId() != null) {
            return checkinInfoMapper.updateByPrimaryKeySelective(checkinInfo);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#refreshUserToken(java
     * .lang.Integer, java.lang.String)
     */
    @Override
    public String refreshUserToken(Integer userId, String token, Integer platform) {
        if (userId != null && StringUtils.isNotBlank(token)) {
            try {
                String newToken = UUID.randomUUID().toString().replaceAll("-", "");
                UserLog userLog = new UserLog();
                userLog.setUserId(userId);
                userLog.setToken(token);

                UserLog lastLog = userLogMapper.getlastBySelective(userLog);
                if (lastLog != null && lastLog.getLogoutTime() == null) {
                    lastLog.setToken(newToken);
                    if (userLogMapper.updateByPrimaryKeySelective(lastLog) > 0) {
                        UserRedisSource.setUserToken(userId, newToken, platform, 0);
                    }
                    return newToken;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("UserService.refreshUserToken is error ：" + e);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#checkUserToken(java.lang
     * .Integer, java.lang.String)
     */
    @Override
    public boolean checkUserToken(Integer userId, String token) {
        if (userId != null && StringUtils.isNotBlank(token)) {
            try {
                UserLog userLog = new UserLog();
                userLog.setUserId(userId);
                UserLog lastLog = userLogMapper.getlastBySelective(userLog);
                if (lastLog != null && lastLog.getLogoutTime() == null) {
                    if (lastLog.getToken().equals(token)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("UserService.checkUserToken is error ：" + e);
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#studentRegister(com.melot
     * .talkee.driver.domain.StudentInfo, java.lang.Integer, java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public Integer studentRegister(StudentInfo studentInfo, Integer channel, Integer recommended, Integer platform, String password) {
        // init userInfo
        String loginName = "";
        try {
            if (StringUtils.isBlank(studentInfo.getPhoneNum()) && StringUtils.isBlank(studentInfo.getEmail())) {
                logger.error("UserService.studentRegister loginName is null ");
                return null;
            }
            if (StringUtils.isBlank(password)) {
                logger.error("UserService.studentRegister password is null ");
                return null;
            }
            UserInfo user;
            if (StringUtils.isNotBlank(studentInfo.getPhoneNum())) {

                String phoneNum = studentInfo.getPhoneNum();
                loginName = phoneNum;
                user = getUserInfoByPhoneNumber(phoneNum);
                if (user != null) {
                    logger.error("UserService.studentRegister " + phoneNum + " is exist ");
                    return null;
                }
            }

            if (StringUtils.isNotBlank(studentInfo.getEmail())) {
                String email = studentInfo.getEmail();
                loginName = email;
                user = getUserInfoByEmail(email);
                if (user != null) {
                    logger.error("UserService.studentRegister " + email + " is exist ");
                    return null;
                }
            }
            user = new UserInfo();
            user.setAccountType(1);
            user.setChannelId(channel);
            user.setTerminal(platform);
            user.setLoginName(loginName);
            user.setEmail(studentInfo.getEmail());
            user.setPhoneNum(studentInfo.getPhoneNum());
            user.setRegisterTime(new Date());
            if (userRegisterMapper.insertUserRegister(user) > 0) {
                Integer userId = user.getUserId();
                studentInfo.setUserId(userId);
                studentInfo.setUpdateTime(new Date());
                modifyPassword(userId, password);
                studentInfo.setUserType(studentInfo.getUserType() == null ? -1 : studentInfo.getUserType());
                studentInfo.setClassType(studentInfo.getClassType() == null ? 0 : studentInfo.getClassType());
                // initStudent
                if (userStudentMapper.insertSelective(studentInfo) > 0) {
                    // init checkin
                    int userType = 1; // 学生
                    if (studentInfo.getUserType() > 0) {
                        initCheckinInfo(userId, userType);
                    }
                    // init student_period
                    initStudentPeriods(userId, 1, 3);
                    return userId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UserService.studentRegister is error ：" + e);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#teacherRegister(com.melot
     * .talkee.driver.domain.TeacherInfo, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Integer teacherRegister(TeacherInfo teacherInfo, Integer channel, Integer platform, String password) {
        try {
            if (StringUtils.isBlank(teacherInfo.getEmail())) {
                logger.error("TalkUserService.teacherRegister teacher.email is null");
                return null;
            }
            if (StringUtils.isBlank(password)) {
                logger.error("UserService.teacherRegister password is null ");
                return null;
            }
            UserInfo user = getUserInfoByEmail(teacherInfo.getEmail());

            if (user != null) {
                logger.error("TalkUserService.teacherRegister teacher.email is exist");
                return null;
            }

            int accountType = 2;// 老师

            user = new UserInfo();
            user.setRegisterTime(new Date());
            user.setChannelId(channel);
            user.setTerminal(platform);
            user.setEmail(teacherInfo.getEmail());
            user.setAccountType(accountType);
            // 登录名为邮箱
            user.setLoginName(teacherInfo.getEmail());
            if (userRegisterMapper.insertUserRegister(user) > 0) {
                int userId = user.getUserId();
                modifyPassword(userId, password);
                teacherInfo.setTeacherId(userId);
                teacherInfo.setAddTime(new Date());
                teacherInfo.setTag(0);
                // init checkin
                if (userTeacherMapper.insertSelective(teacherInfo) > 0) {
                    initCheckinInfo(userId, accountType);
                    // 更新可教课程等级
                    updateTeacherLessonLevel(userId, teacherInfo.getCanteacherLevels());
                    return userId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TalkUserService.teacherRegister is error:" + e.getLocalizedMessage());
        }
        return null;
    }

    private void updateTeacherLessonLevel(int teacherId, String lessonLevels) {
        teacherLessonMapper.deleteByTeacherId(teacherId);
        if (StringUtils.isBlank(lessonLevels)) {
            return;
        }
        String[] levels = lessonLevels.split(",");
        for (String level : levels) {
            TeacherLesson teacherLesson = new TeacherLesson();
            teacherLesson.setLessonLevel(Integer.valueOf(level));
            teacherLesson.setTeacherId(teacherId);
            teacherLessonMapper.insertSelective(teacherLesson);
        }

    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#adminRegister(java.lang
     * .String, java.lang.String, java.lang.Integer)
     */
    @Override
    public Integer adminRegister(String cnNickname, String enNickname, Integer adminId, String password) {
        try {
            if (adminId == 0) {
                logger.info("TalkUserService.adminRegister admin is invalid");
                return null;
            }
            if (StringUtils.isBlank(password)) {
                logger.info("TalkUserService.adminRegister password is null");
                return null;
            }

            AdminInfo admin = adminInfoMapper.selectByAdminId(adminId);

            if (admin != null) {
                logger.info("TalkUserService.adminRegister admin is exist");
                return null;
            }
            Date eventTime = new Date();
            UserInfo user = new UserInfo();
            int accountType = 3;
            user.setAccountType(accountType);
            user.setRegisterTime(eventTime);
            // 注册成功
            if (userRegisterMapper.insertUserRegister(user) > 0) {
                int userId = user.getUserId();
                modifyPassword(userId, password);
                admin = new AdminInfo();
                admin.setAdminId(adminId);
                admin.setCnNickname(cnNickname);
                admin.setEnNickname(enNickname);
                admin.setUserId(userId);
                admin.setAddTime(eventTime);
                if (adminInfoMapper.insert(admin) > 0) {
                    return userId;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("TalkUserService.adminRegister is error:" + e.getLocalizedMessage());
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getAdminCityById(java
     * .lang.Integer)
     */
    @Override
    public AdminCity getAdminCityById(Integer cityId) {
        if (cityId != null) {
            String cacheKey = "city_"+cityId;
            AdminCity adminCity = cacheService.getFromCache(cacheKey,AdminCity.class);
            if (adminCity == null) {
                adminCity = adminCityMapper.selectByPrimaryKey(cityId);
                if (adminCity != null) {
                    try {
                        cacheService.putInCache(cacheKey, adminCity);
                    } catch (Exception e) {
                        logger.error("cache AdminCity cityId :"+cityId+" is error ",e);
                    }
                }
            }
            return adminCity;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getAdminCityByParentId
     * (java.lang.Integer)
     */
    @Override
    public List<AdminCity> getAdminCityByParentId(Integer parentId) {
        if (parentId == null) {
            parentId = 0;
        }
        return adminCityMapper.selectByParentId(parentId);
    }

    /**
     * 修改用户等级
     * 从试听-->普通
     *
     * @param userId
     * @param toUserType
     * @return
     */
    @Override
    @Transactional
    public String modifyUserType(int userId, int toUserType) {
        String tagCode = TalkTagCodeEnum.SUCCESS;
        if (userId == 0 || getStudentInfoByUserId(userId) == null) {
            return TalkTagCodeEnum.USER_NOT_EXIST;
        }
        StudentInfo studentInfo = getStudentInfoByUserId(userId);
        int userType = studentInfo.getUserType();

        if (userType == toUserType) {
            return tagCode;
        }
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("status", 1);
            List<BuyPeriods> list = buyPeriodsMapper.selectByParamByUserIdAndStatus(params);

            int periodsCount = 0;
            int publicCount = 0;
            int validateDays = 0;
            OrderPackage pack = null;
            Date startDate = null;
            for (BuyPeriods buyPeriods : list) {
                if (startDate == null) {
                    startDate = buyPeriods.getBuyTime();
                }
                periodsCount = periodsCount + buyPeriods.getExtraPeriods() + buyPeriods.getPeriods();
                publicCount = publicCount + buyPeriods.getExtraPublicLesson() + buyPeriods.getPublicLesson();
                pack = orderPackageMapper.selectByPrimaryKey(buyPeriods.getPackageId());
                validateDays = validateDays > pack.getVaildDays() ? validateDays : pack.getVaildDays();
                startDate = startDate.getTime() < buyPeriods.getBuyTime().getTime() ? startDate : buyPeriods.getBuyTime();
            }
            if (validateDays > 0) {
                if (periodsCount > 0) {
                    StudentPeriods periodsLesson = new StudentPeriods();
                    periodsLesson.setAllPeriods(periodsCount);
                    periodsLesson.setUserId(userId);
                    periodsLesson.setDtime(new Date());
                    periodsLesson.setBeginTime(startDate);
                    periodsLesson.setEndTime(DateUtils.addDays(startDate, validateDays));
                    periodsLesson.setPeriodsType(TalkCommonEnum.PERIOD_TYPE_GENNEL);
                    periodsLesson.setCurPeriods(periodsCount);
                    periodsLesson.setVaildPreviwPeriods(periodsCount);

                    /**
                     * 累计课程时长
                     */
                    Map<String, Object> param = new HashMap<>();
                    param.put("userId",userId);
                    param.put("periodType",TalkCommonEnum.PERIOD_TYPE_GENNEL);
                    int count = studentPeriodsMapper.selectCountByUserIdAndLessonType(param);

                    periodsLesson.setAccumulativeTotal(25 * 60 * 1000 * count);

                    studentPeriodsMapper.insert(periodsLesson);
                }
                if (publicCount > 0) {
                    StudentPeriods publicLesson = new StudentPeriods();
                    publicLesson.setAllPeriods(periodsCount);
                    publicLesson.setUserId(userId);
                    publicLesson.setDtime(new Date());
                    publicLesson.setBeginTime(startDate);
                    publicLesson.setEndTime(DateUtils.addDays(startDate, validateDays));
                    publicLesson.setPeriodsType(TalkCommonEnum.PERIOD_TYPE_PUBLIC);
                    publicLesson.setCurPeriods(periodsCount);
                    publicLesson.setVaildPreviwPeriods(periodsCount);

                    /**
                     * 累计课程时长
                     */
                    Map<String, Object> param = new HashMap<>();
                    param.put("userId",userId);
                    param.put("periodType",TalkCommonEnum.PERIOD_TYPE_PUBLIC);
                    int count = studentPeriodsMapper.selectCountByUserIdAndLessonType(param);

                    publicLesson.setAccumulativeTotal(25 * 60 * 1000 * count);

                    studentPeriodsMapper.insert(publicLesson);
                }
            }
        } catch (Exception e) {
            logger.error("TalkUserServiceImpl.modifyUserType is error," + e.getLocalizedMessage());
        }
        return tagCode;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getAdminInfoByAdminId
     * (int)
     */
    @Override
    public AdminInfo getAdminInfoByAdminId(Integer adminId) {
        if (adminId != null) {
            return adminInfoMapper.selectByAdminId(adminId);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.melot.talkee.driver.service.TalkUserService#getAdminInfoByUserId(
     * java.lang.Integer)
     */
    @Override
    public AdminInfo getAdminInfoByUserId(Integer userId) {
        if (userId != null) {
            return adminInfoMapper.selectByPrimaryKey(userId);
        }
        return null;
    }

}
