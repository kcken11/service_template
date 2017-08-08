package com.melot.talkee.driver.service;

import com.melot.talkee.driver.domain.*;

import java.util.List;
import java.util.Map;

/**
 * Title: UserService
 * Description: 用户服务接口
 *
 * @author 董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-01 09:25:53
 */
public interface TalkUserService {

    /**
     * 根据手机号码获取注册信息
     *
     * @param phoneNum
     * @return 用户信息
     */
    UserInfo getUserInfoByPhoneNumber(String phoneNum);

    /**
     * 根据userId获取注册信息
     *
     * @param userId
     * @return 用户信息
     */
    UserInfo getUserInfoByUserId(Integer userId);

    /**
     * 根据userId获取学生信息
     *
     * @param studentId
     * @return 学生信息
     */
    StudentInfo getStudentInfoByUserId(Integer studentId);

    /**
     * 根据teacherId获取老师信息
     *
     * @param teacherId
     * @return 老师信息
     */
    TeacherInfo getTeacherInfoByUserId(Integer teacherId);

    /**
     * 根据登录用户名获取注册信息
     *
     * @param loginName
     * @return 用户信息
     */
    UserInfo getUserInfoByLoginName(String loginName);

    /**
     * 根据email获取注册信息
     *
     * @param email
     * @return 用户信息
     */
    UserInfo getUserInfoByEmail(String email);

    /**
     * 手机号码注册
     *
     * @param channel
     * @param recommended
     * @param platform
     * @param port
     * @param phoneNum
     * @param deviceUId
     * @param version
     * @param clientIp
     * @param pwd
     * @return 用户userId
     */
    Integer registerViaPhoneNum(Integer channel, Integer recommended, Integer platform, Integer port, String phoneNum, String deviceUId, String version, String clientIp, String pwd);


    /**
     * 学生注册
     *
     * @param studentInfo 学生信息
     * @param channel     渠道
     * @param recommended 推荐人
     * @param platform    平台
     * @param password    六位随机密码 1位字母+5位随机数
     * @return 用户ID
     */
    Integer studentRegister(StudentInfo studentInfo, Integer channel, Integer recommended, Integer platform, String password);


    /**
     * 老师注册
     *
     * @param teacherInfo 学生信息
     * @param channel     渠道
     * @param platform    平台
     * @param password    六位随机密码 1位字母+5位随机数
     * @return 用户ID
     */
    Integer teacherRegister(TeacherInfo teacherInfo, Integer channel, Integer platform, String password);

    /**
     * 管理员注册
     *
     * @param cnNickname 中文昵称
     * @param enNickname 英文昵称
     * @param adminId    管理员id
     * @param password   六位随机密码 1位字母+5位随机数
     * @return 用户ID
     */
    Integer adminRegister(String cnNickname, String enNickname, Integer adminId, String password);

    /**
     * 修改用户登录名
     *
     * @param userId
     * @param loginName
     */
    Integer modifyLoginName(Integer userId, String loginName);

    /**
     * 修改学生信息
     *
     * @param loginName
     * @param userStudent
     * @return 执行结果大于0成功
     */
    Integer modifyStudentInfo(String loginName, StudentInfo userStudent);

    /**
     * 修改老师信息
     *
     * @param loginName
     * @param userTeacher
     * @return 执行结果大于0成功
     */
    Integer modifyTeacherInfo(String loginName, TeacherInfo userTeacher);


    /**
     * 修用户密码
     *
     * @param userId
     * @param pwd    密码明文
     * @return 执行结果
     */
    boolean modifyPassword(Integer userId, String pwd);

    /**
     * 文件上传
     *
     * @param userId
     * @param fileType
     * @param files
     * @return 上传结果
     */
    boolean fileUpload(Integer userId, Integer fileType, Map<String, String> files, Map<String, Object> paramMap);

    /**
     * 文件上传(CRM用户头像上传)
     *
     * @param userId
     * @param fileType
     * @param files
     * @return 上传结果
     */
    boolean adminFileUpload(Integer userId, Integer fileType, Map<String, String> files, Map<String, Object> paramMap);

    /**
     * 记录登录流水
     *
     * @param userId
     * @param loginType
     * @param platform
     * @param loginIp   登录ip
     * @return
     */
    String recordLogin(Integer userId, Integer loginType, Integer platform, String loginIp);

    /**
     * 刷新用户token
     *
     * @param userId
     * @param token
     * @param platform
     * @return 新用户token
     */
    String refreshUserToken(Integer userId, String token, Integer platform);


    /**
     * 检测用户token
     *
     * @param userId
     * @param token
     * @return 检验结果
     */
    boolean checkUserToken(Integer userId, String token);

    /**
     * 记录注销流水
     *
     * @param userId
     * @param token
     * @return
     */
    void recordLogout(Integer userId, String token, Integer platform);

    /**
     * 通过userId获取头像
     *
     * @param userId
     * @return 头像地址
     */
    String getPortraitByUserId(Integer userId);

    /**
     * 通过userId获取头像(CRM员工头像)
     *
     * @param userId
     * @param type   资源类型
     * @return 头像地址
     */
    String getPortraitByUserIdAndType(Integer userId, Integer type);

    /**
     * 获取用户课时
     *
     * @param userId
     * @param periodType
     * @return 用户课时信息
     */
    StudentPeriods getStudentPeriods(Integer userId, Integer periodType);


    /**
     * 获取用户考勤信息
     *
     * @param userId
     * @return 用户考勤信息
     */
    CheckinInfo getCheckinInfo(Integer userId);


    /**
     * 修改用户课时（主键必须存在）
     *
     * @param studentPeriods
     * @return 执行结果大于0成功
     */
    int modifyStudentPeriods(StudentPeriods studentPeriods);


    /**
     * 获取用户考勤信息（主键必须存在）
     *
     * @param checkinInfo
     * @return 执行结果大于0成功
     */
    int modifyCheckinInfo(CheckinInfo checkinInfo);

    /**
     * 通过城市Id获取城市区域信息
     *
     * @param cityId
     * @return 城市区域信息
     */
    AdminCity getAdminCityById(Integer cityId);

    /**
     * 通过城市区域父ID获取城市区域信息列表
     *
     * @param parentId （为空默认为0）
     * @return 城市区域信息列表
     */
    List<AdminCity> getAdminCityByParentId(Integer parentId);


    /**
     * 修改用户类型
     *
     * @param userId
     * @param toUserType
     * @return tagcode
     */
    String modifyUserType(int userId, int toUserType);

    /**
     * 通过adminId 查询CC或CE信息
     *
     * @param adminId
     * @return
     */
    AdminInfo getAdminInfoByAdminId(Integer adminId);


    /**
     * 通过UserId 查询CC或CE信息
     *
     * @param userId
     * @return
     */
    AdminInfo getAdminInfoByUserId(Integer userId);

}


