package com.melot.talkee.schedule;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.melot.talkee.dao.TeacherPerformanceMapper;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.service.TalkEmailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.melot.talkee.dao.StudentLessonMapper;
import com.melot.talkee.dao.TurnStudentLessonMapper;
import com.melot.talkee.driver.service.TalkOrderService;
import com.melot.talkee.driver.service.TalkPublishService;
import com.melot.talkee.redis.LessonRedisSource;

/**
 * Title: CheckLessonStateSchedule
 * Description: 检查课程状态
 *
 * @author 董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-05-05 09:44:53
 */
public class CheckLessonStateSchedule {

    private Logger logger = Logger.getLogger(CheckLessonStateSchedule.class);

    @Autowired
    private TalkPublishService talkPublishService;

    @Autowired
    private TalkOrderService talkOrderService;

    @Autowired
    private StudentLessonMapper studentLessonMapper;

    @Autowired
    private TurnStudentLessonMapper turnStudentLessonMapper;

    @Autowired
    private TeacherPerformanceMapper teacherLessonHistMapper;

    @Autowired
    private TalkEmailService talkEmailService;




    public void checkLessonState() {
        try {
            // 获取当天需要上课的老师列表
            Set<String> dailyTeacherList = LessonRedisSource.getDailyTeacherSet();
            if (dailyTeacherList != null && dailyTeacherList.size() > 0) {
                for (String teacherId : dailyTeacherList) {

                    // 获取获取老师待上课程列表
                    Set<String> periodIdList = LessonRedisSource.getTeacherDailyLessonSet(teacherId);
                    if (periodIdList != null && periodIdList.size() > 0) {
                        PublishLesson publishLesson = null;
                        for (String periodId : periodIdList) {

                            // 获取对应课程详细信息
                            publishLesson = talkPublishService.getPublishLessonById(Integer.valueOf(periodId));
                            if (publishLesson != null) {

                                // 获取当前发布课程状态，防止有取消预约操作
                                int state = publishLesson.getState();
                                if (state == TalkCommonEnum.PUBLISH_LESSON_STATE_ORDER) {

                                    // 计算当前课程是否已经结束
                                    Date endTime = publishLesson.getEndTime();
                                    Date beginTime = publishLesson.getBeginTime();
                                    long currentTime = new Date().getTime();
                                    if (currentTime > endTime.getTime()) {

                                        // 删除当前老师待上课列表
                                        LessonRedisSource.removeTeacherDailyLessonSet(teacherId, periodId);

                                        // 防止多线程处理同一个问题
                                        if (LessonRedisSource.getCheckLessonStatus(teacherId, periodId) == 1) {
                                            talkOrderService.checkinOverLesson(Integer.valueOf(periodId));
                                        }
                                    } else {
                                        if (currentTime > beginTime.getTime()) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("stateList", Arrays.asList(0));
                                            map.put("teacherId", Integer.valueOf(teacherId));
                                            map.put("periodList", Arrays.asList(Integer.valueOf(periodId)));
                                            List<OrderLesson> list = studentLessonMapper.getOrderLesson(map);
                                            if (list != null && list.size() > 0) {
                                                StudentLesson studentLesson = null;
                                                TurnStudentLesson turnStudentLesson = null;
                                                int conPriodType = 1;
                                                for (OrderLesson orderLesson : list) {
                                                    if (orderLesson.getLessonState() != 0) {
                                                        continue;
                                                    }
                                                    conPriodType = orderLesson.getPublishType();
                                                    if (conPriodType == TalkCommonEnum.PERIOD_TYPE_TRY) {
                                                        turnStudentLesson = new TurnStudentLesson();
                                                        turnStudentLesson.setHistId(orderLesson.getHistId());
                                                        turnStudentLesson.setTrunVideoState(1);
                                                        turnStudentLessonMapper.updateByPrimaryKeySelective(turnStudentLesson);
                                                    } else {
                                                        studentLesson = new StudentLesson();
                                                        studentLesson.setHistId(orderLesson.getHistId());
                                                        studentLesson.setLessonState(1);
                                                        studentLessonMapper.updateByPrimaryKeySelective(studentLesson);
                                                    }
                                                }
                                            }
                                        }
                                        //1小时倒计时提醒
                                        if (beginTime.getTime() - currentTime <= 60 * 60 * 1000) {
                                            if(teacherLessonHistMapper.selectByPeriodId(publishLesson.getPeriodId())==null) {
                                                  //发送上课通知
                                                  //预约成功,初始化老师流水
                                                  TeacherPerformance teacherLessonHist = new TeacherPerformance();
                                                  teacherLessonHist.setDtime(beginTime);
                                                  teacherLessonHist.setTeacherId(publishLesson.getTeacherId());
                                                  teacherLessonHist.setNeedPeriods(0);
                                                  teacherLessonHist.setVaildPeriods(0);
                                                  teacherLessonHist.setRealPeriods(0);
                                                  teacherLessonHist.setExtraPeriods(0);
                                                  teacherLessonHist.setPeriodId(publishLesson.getPeriodId());
                                                  teacherLessonHist.setLessonType(publishLesson.getPublishType());
                                                  teacherLessonHistMapper.insertSelective(teacherLessonHist);
                                            }
                                            //判断key是否存在
                                            if(!LessonRedisSource.existInClassKey(publishLesson.getPeriodId())){
                                                talkEmailService.sendTemplateMail(EmailAdviceTypeEnum.IN_CLASS, publishLesson.getPeriodId(),publishLesson.getStudentId(), 0);
                                                LessonRedisSource.expireInClassKey(publishLesson.getPeriodId(),24*3600);
                                            }
                                        }
                                    }
                                } else {

                                    // 已取消预约
                                    LessonRedisSource.removeTeacherDailyLessonSet(teacherId, periodId);
                                }
                            } else {

                                // 信息缺失删除当前老师待上课列表
                                LessonRedisSource.removeTeacherDailyLessonSet(teacherId, periodId);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
