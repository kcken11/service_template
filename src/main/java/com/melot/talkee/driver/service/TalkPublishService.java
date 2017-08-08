package com.melot.talkee.driver.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.PublishLesson;
import com.melot.talkee.driver.domain.PublishLessonCount;

/**
 * Title: PublishService
 * Description: 课程发布服务接口
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-06 11:25:53 
 */
public interface TalkPublishService {
	
	
	/**
	 * 课程发布
	 * @param teacherId 老师ID
	 * @param lessonTime Map<开始时间，结束时间>
	 * @param publishType 发布类型 1:普通课 2:公开课 3:调试课
	 * @param maxNum 最大人数
	 * @param lessonId 课程ID
	 * @return TalkTagCodeEnum 错误码
	 */
	String publishLesson(Integer teacherId,Map<Long, Long> lessonTime,Integer publishType,Integer maxNum,Integer lessonId);
	
	/**
	 * 通过课程ID获取课程发布信息
	 * @param periodId
	 * @return 发布信息
	 */
	PublishLesson getPublishLessonById(Integer periodId);
	
	/**
	 * 获取对应课程日期内的课程发布信息
	 * @param teacherId 老师ID
	 * @param lessonDate 课程日期（对应时区零点）
	 * @param state 状态 1 可预约 2 已预约
	 * @param publishType 1:普通课 2:公开课 3:调试课
	 * @return 发布课程详细信息列表
	 */
	List<PublishLesson> getDailyPublishLessonList(Integer teacherId,Date lessonDate,Integer state,Integer publishType);
	
	/**
	 * 通过开始时间结束时间获取每日发布课程发布、预约数
	 * @param teacherId 老师ID
	 * @param beginTime 开始时间 （对应时区0点）
	 * @param endTime 结束时间 （对应时区24点）
	 * @param state 状态 1 可预约 2 已预约
	 * @param publishType 发布类型 1:普通课 2:公开课 3:调试课
	 * @return 发布课程汇总列表
	 */
	List<PublishLessonCount> getPublishLessonList(Integer teacherId,Date beginTime, Date endTime,Integer state,Integer publishType);
	
	
	/**
	 * 批量修改课程发布信息(主键必填)
	 * @param teacherId 老师ID
	 * @param lessonTime Map<开始时间，结束时间>
	 * @param publishType 发布类型
	 * @param maxNum 最大人数
	 * @param lessonId 课程ID
	 * @return 课程详细信息列表
	 */
	List<PublishLesson>  modifyPublishLesson(Integer teacherId,Date lessonDate,Map<Long, Long> lessonTime,Integer publishType,Integer maxNum,Integer lessonId);

}
