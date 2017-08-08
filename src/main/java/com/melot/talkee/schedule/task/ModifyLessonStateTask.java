package com.melot.talkee.schedule.task;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.melot.talkee.dao.ActionLessioningMapper;
import com.melot.talkee.dao.StudentPeriodsMapper;
import com.melot.talkee.driver.domain.ActionLessioning;
import com.melot.talkee.driver.domain.OrderLesson;
import com.melot.talkee.driver.domain.StudentPeriods;
import com.melot.talkee.driver.service.TalkOrderService;
import com.melot.talkee.driver.service.TalkUserService;

public class ModifyLessonStateTask extends Thread{
	
	private Logger logger = Logger.getLogger(ModifyLessonStateTask.class);
	
	private int teacherId;
	private int periodId;
	
	@Autowired
	private TalkOrderService talkOrderService;
	
	@Autowired
	private TalkUserService talkUserService;
	
	@Autowired
	private ActionLessioningMapper actionLessioningMapper;
	
	@Autowired
	private StudentPeriodsMapper studentPeriodsMapper;
	
	public ModifyLessonStateTask(int teacherId,int periodId){
		this.teacherId = teacherId;
		this.periodId = periodId;
	}
	
	@Override
	public void run() {
		try {
			// 获取该课程对应预约信息
			List<OrderLesson> orderLessonList = talkOrderService.getOrderLessonByPeriodList(Arrays.asList(Integer.valueOf(periodId)));
//			if (orderLessonList != null && orderLessonList.size() > 0) {
//				
//				Map<String, Object> param = new HashMap<String, Object>();
//				param.put("teacherId", teacherId);
//				param.put("periodId", periodId);
//				
//				// 获取当前老师对应课程的inout记录
//				ActionLessioning actionLessioning = null;
//				for (OrderLesson orderLesson : orderLessonList) {
//					int studentId = orderLesson.getStudentId();
//					param.put("studentId", studentId);
//					
//					// 查询当前学生课时数
//					StudentPeriods studentPeriods = studentPeriodsMapper.selectByUserId(studentId);
//					
//					actionLessioning = actionLessioningMapper.selectByPeriodAndUser(param);
//					if (actionLessioning != null) {
//						
//					}else{
//						// 表示学生老师都迟到，扣除当前学生剩余课时，上完课程不加，学生等级不加，标记当前预约课程完成，老师课时加一
//						if (studentPeriods != null) {
//							studentPeriods.setCurPeriods(studentPeriods.getCurPeriods() - 1);
//							studentPeriodsMapper.updateByPrimaryKeySelective(studentPeriods);
//						}
//						actionLessioning = new ActionLessioning(periodId, studentId, teacherId, orderLesson.getBeginTime(), orderLesson.getEndTime());
//						actionLessioning.setState(2);
//						int resultCode = actionLessioningMapper.insertSelective(actionLessioning);
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
