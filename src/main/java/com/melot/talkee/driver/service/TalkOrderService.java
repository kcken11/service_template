package com.melot.talkee.driver.service;


import java.util.Date;
import java.util.List;

import com.melot.talkee.driver.domain.*;

/**
 * Title: OrderService
 * Description: 课程预约服务接口
 *
 * @author 董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-07 10:25:53
 */
public interface TalkOrderService {


    /**
     * 预约课程
     *
     * @param userId 学生ID
     * @param periodList 课时ID列表
     * @param orderUser 预约人（CC\CR\EA）
     * @return TalkTagCodeEnum 错误码
     */
    String orderLesson(Integer userId, List<Integer> periodList, Integer orderUser);

    /**
     * 取消预约课程
     *
     * @param userId 学生ID
     * @param periodId 课时ID
     * @param cancleReason 取消原因
     * @param cancleUser 取消人
     * @return TalkTagCodeEnum 错误码
     */
    String cancleOrder(Integer userId, Integer periodId, String cancleReason, Integer cancleUser);

    /**
     * 获取课程列表(学生端,待上课，已上课)
     *
     * @param userId
     * @param lessonState
     * @param beginTime
     * @param endTime
     * @param start
     * @param offset
     * @return 预约课程列表（分页）
     */
    Pager<OrderLesson> getPagerOrderLessonList(Integer userId, Integer lessonState, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset,String order);

    /**
     * 已上课表(老师端)
     *
     * @param userId
     * @param beginTime
     * @param endTime
     * @param publishType
     * @param start
     * @param offset
     * @param order
     * @return
     */
    Pager<OrderLesson> getOverLessonList(Integer userId, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset,String order);

    /**
     * 请假课表(老师端)
     *
     * @param userId
     * @param beginTime
     * @param endTime
     * @param publishType
     * @param start
     * @param offset
     * @param order
     * @return
     */
    Pager<OrderLesson> getLeaveLessonList(Integer userId, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset,String order);

    /**
     * 待上课表(老师端)
     *
     * @param userId
     * @param beginTime
     * @param endTime
     * @param publishType
     * @param start
     * @param offset
     * @param order
     * @return
     */
    Pager<OrderLesson> getAwaitLessonList(Integer userId, Date beginTime, Date endTime, Integer publishType, Integer start, Integer offset,String order);


    /**
     * 获取课程列表
     *
     * @param userId
     * @param lessonState
     * @param beginTime
     * @param endTime
     * @return 预约课程列表
     */
    List<OrderLesson> getOrderLessonList(Integer userId, Integer lessonState, Date beginTime, Date endTime, Integer publishType);

    /**
     * 通过课时Id列表获取课程列表
     *
     * @param periodList
     * @return 预约课程列表
     */
    List<OrderLesson> getOrderLessonByPeriodList(List<Integer> periodList);

    /**
     * 获取老师完结课程汇汇总信息
     * @param teacherId
     * @return TeacherOverPeriodCount
     */
    TeacherOverPeriodCount getTeacherOverPeriodCount(Integer teacherId);

    /**
     * 添加评论
     *
     * @param periodId
     * @param studentId
     * @param questionId
     * @param comment
     * @return 执行结果 大于0 成功
     */
    int addComment(Integer periodId, Integer studentId, Integer questionId, String comment);

    /**
     * 获取对应课程详细评论
     *
     * @param periodId
     * @param studentId
     * @return 老师评论详细列表
     */
    List<TeacherDetailComment> getDetailCommentList(Integer periodId, Integer studentId);

    /**
     * 记录完结课程状态
     *
     * @param periodId
     * @return
     */
    boolean checkinOverLesson(Integer periodId);

    /**
     * 修改完结课程状态
     *
     * @param userId      责任归属人
     * @param periodId
     * @param lessonState 2：课程正常 3：旷课 4：迟到 5：严重迟到  6:早退 7：课程异常 8:老师严重+1 9：课程异常（非人为）
     * @return
     */
    boolean modifyOverLessonStatus(Integer userId, Integer periodId, Integer lessonState);

    /**
     * 查询用户有效课时
     * @param userId
     * @return
     */
    public String queryUserInfo(int userId);

    /**
     * 查询课程相关基本
     * @param periodId
     * @return
     */
    public StudentLesson queryStudentLessonByPeriodId(Integer periodId);

    /**
     * 修改用户等级
     * @param studentId
     * @param userLevel
     * @param subLevel
     * @return
     */
    boolean modifyUserLevel(Integer studentId, Integer userLevel, Integer subLevel);
}
