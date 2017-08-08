package com.melot.talkee.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.melot.talkee.dao.PublishLessonMapper;
import com.melot.talkee.driver.domain.PublishLesson;
import com.melot.talkee.driver.domain.TalkCommonEnum;
import com.melot.talkee.redis.LessonRedisSource;

/**
 * Title: CacheDailyLessonSchedule
 * Description: 缓存当天已预约课程状态
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-05-04 14:44:53 
 */
public class CacheDailyLessonSchedule {

	private Logger logger = Logger.getLogger(CacheDailyLessonSchedule.class);
	
	@Autowired
	private PublishLessonMapper publishLessonMapper;
	
	public void refresh(){
		logger.info("start refresh publish lesson ");
		// 查询当天不可预约老师发布课程
		
		List<PublishLesson> publishLessonList = publishLessonMapper.getLatePublishLesson(TalkCommonEnum.PUBLISH_LESSON_STATE_ORDER);
		List<Integer> periodList = new ArrayList<Integer>();
		if (publishLessonList != null && publishLessonList.size() > 0) {
			for (PublishLesson publishLesson : publishLessonList) {
				try {
					int teacherId = publishLesson.getTeacherId();
					Date beginTime = publishLesson.getBeginTime();
					int periodId = publishLesson.getPeriodId();
					
					periodList.add(periodId);
					// 保存每天待上课老师
					LessonRedisSource.addDailyTeacherSet(teacherId);
					
					// 保存每天待上课老师对应课程 按开始时间排序
//					if (endTime.getTime() > new Date().getTime()) {
						LessonRedisSource.addTeacherDailyLessonSet(teacherId, periodId, beginTime);
//					}
				} catch (Exception e) {
					logger.error("LessonRedisSource.CacheDailyLessonSchedule is error :" + e);
				}
			}
		}
		logger.info("end refresh publish lesson id "+new Gson().toJson(periodList));
	}
}
