package com.melot.talkee.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.melot.kktv.util.DateUtil;
import com.melot.talkee.dao.AdminInfoMapper;
import com.melot.talkee.dao.PublishLessonMapper;
import com.melot.talkee.driver.domain.AdminInfo;
import com.melot.talkee.driver.domain.PublishLesson;
import com.melot.talkee.driver.domain.PublishLessonCount;
import com.melot.talkee.driver.domain.TalkCommonEnum;
import com.melot.talkee.driver.domain.TalkTagCodeEnum;
import com.melot.talkee.driver.domain.TeacherInfo;
import com.melot.talkee.driver.domain.UserInfo;
import com.melot.talkee.driver.service.TalkPublishService;
import com.melot.talkee.driver.service.TalkUserService;
import com.melot.talkee.redis.LessonRedisSource;

@Service
public class TalkPublishServiceImpl implements TalkPublishService{
	
	private static final Logger LOGGER = Logger.getLogger(TalkPublishServiceImpl.class);
	
	@Autowired
	private TalkUserService talkUserService;
	
	@Autowired
	private PublishLessonMapper publishLessonMapper;
	
    @Autowired
    private AdminInfoMapper adminInfoMapper;

	@Override
	@Transactional
	public String publishLesson(Integer publishUserId,Map<Long, Long> lessonTime,Integer publishType, Integer maxNum,Integer lessonId) {
		String tagCode = TalkTagCodeEnum.SUCCESS;
		if (publishUserId != null && publishType != null && lessonTime != null && lessonTime.size() > 0) {
			// 非普通课课程ID 必填
			if (publishType.intValue() > 1 && lessonId == null) {
				return TalkTagCodeEnum.LESSON_ID_IS_NULL;
			}
			Integer teacherId = null;
			if (publishType == 3) {
				// TODO 查询userAdmin
				AdminInfo adminInfo = adminInfoMapper.selectByPrimaryKey(publishUserId);
				if (adminInfo != null) {
					teacherId = adminInfo.getUserId();
				}
			}else{
				TeacherInfo teacherInfo =	talkUserService.getTeacherInfoByUserId(publishUserId);
				if (teacherInfo != null) {
					teacherId = publishUserId;
				}
			}
			if (teacherId != null) {
				PublishLesson publishLesson = null;
				int code = 0;
				for (Map.Entry<Long, Long> entry : lessonTime.entrySet()) {
					publishLesson = new PublishLesson();
					try {
						// 开始时间 大于等于结束时间不添加
						long startTime=Long.parseLong(String.valueOf(entry.getKey()));
						if (startTime >= entry.getValue()) {
							continue;
						}
						publishLesson.setBeginTime(new Date(startTime));
						publishLesson.setEndTime(new Date(entry.getValue()));
					} catch (Exception e) {
						// 开始时间，结束时间异常不添加
						e.printStackTrace();
						continue;
					}
					publishLesson.setTeacherId(teacherId);
					// 通过老师、开始时间、结束时间 查询有交集不添加
					List<PublishLesson> intersectList = publishLessonMapper.isIntersect(publishLesson);
					if (intersectList != null && intersectList.size() > 0) {
						continue;
					}
					publishLesson.setMaxNum(maxNum);
					publishLesson.setPublishTime(new Date());
					publishLesson.setPublishType(publishType);
					publishLesson.setState(1);
					publishLesson.setLessonId(lessonId);
					publishLesson.setOrderNum(0);
					code = publishLessonMapper.insertSelective(publishLesson);
					if (code > 0) {
						// TODO 此处添加默认预约功能，获取当开始时间对应上周、周几的开始时间，查询对应预约人，验证是否空闲课时
						
					}
				}
			}else{
				LOGGER.error("LessonServiceImpl.publishLesson publishUserId:["+publishUserId+"], publishType：["+publishType+"] query data is null");
				tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
			}
		}else {
			LOGGER.error("LessonServiceImpl.publishLesson publishUserId：["+publishUserId+"], lessonTime：["+new Gson().toJson(lessonTime)+"], publishType：["+publishType+"] parameter exception");
			tagCode = TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
		}
		return tagCode;
	}
	
	
	/* (non-Javadoc)
	 * @see com.melot.talkee.driver.service.LessonService#modifyBatchPublishLesson(java.lang.Integer, java.util.Date, java.util.Map, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional
	public List<PublishLesson> modifyPublishLesson(Integer teacherId,Date lessonDate, Map<Long, Long> lessonTime, Integer publishType, Integer maxNum,Integer lessonId) {
		if (teacherId == null || lessonDate == null || publishType == null) {
			return null;
		}
		UserInfo userInfo =	talkUserService.getUserInfoByUserId(teacherId);
		// 只有老师或CE可以发布课程
		if (userInfo == null || userInfo.getAccountType() == null || userInfo.getAccountType().intValue() == 1) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Date endTime = DateUtil.addOnField(lessonDate, Calendar.DATE, 1);
		paramMap.put("teacherId", teacherId);
		paramMap.put("publishType", publishType);
		paramMap.put("beginTime", lessonDate);
		paramMap.put("endTime", endTime);
		paramMap.put("state", 1);
		if (lessonTime != null && lessonTime.size() > 0) {
			List<PublishLesson> list = publishLessonMapper.selectByDaily(paramMap);
			if (list != null && list.size() > 0) {
				for (PublishLesson temp : list) {
					long beginTime = temp.getBeginTime().getTime();
					if (lessonTime.containsKey(beginTime)) {
						// 存在删除发布时间
						lessonTime.remove(beginTime);
					}else{
						// 不存在 删除发布信息
						publishLessonMapper.deleteByPrimaryKey(temp.getPeriodId());
					}
				}
			}
			publishLesson(teacherId, lessonTime, publishType, maxNum,lessonId);
		}else{
			// 删除当天所有未预约的课程
			publishLessonMapper.deleteBySelectiveMap(paramMap);
		}
		return getDailyPublishLessonList(teacherId, lessonDate, null, publishType);
	}
	
	
	/* (non-Javadoc)
	 * @see com.melot.talkee.driver.service.LessonService#getDailyPublishLessonList(java.lang.Integer, java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<PublishLesson> getDailyPublishLessonList(Integer teacherId,Date lessonDate,Integer state, Integer publishType) {
		if (teacherId != null && lessonDate != null) {
			Date endTime = DateUtil.addOnField(lessonDate, Calendar.DATE, 1);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("teacherId", teacherId);
			paramMap.put("publishType", publishType);
			paramMap.put("beginTime", lessonDate);
			paramMap.put("endTime", endTime);
			return publishLessonMapper.selectByDaily(paramMap);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.melot.talkee.driver.service.LessonService#getPublishLessonList(java.lang.Integer, java.lang.Long, java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<PublishLessonCount> getPublishLessonList(Integer teacherId, Date beginTime, Date endTime, Integer state, Integer publishType) {
		
		if (beginTime == null || endTime == null) {
			return null;
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		param.put("state", state);
		param.put("publishType", publishType);
		param.put("teacherId", teacherId);
		List<PublishLesson> countList = publishLessonMapper.getMonthCountBySelective(param);
		
		Map<Long, PublishLessonCount> monthCountMap = new HashMap<Long, PublishLessonCount>();
		PublishLessonCount publishLessonCount = null;
		Date dailyBeginTime = beginTime;
		while (dailyBeginTime.getTime() < endTime.getTime()) {
			publishLessonCount = new PublishLessonCount(dailyBeginTime.getTime(), 0, 0, teacherId, publishType);
			monthCountMap.put(dailyBeginTime.getTime(), publishLessonCount);
			
			dailyBeginTime = DateUtil.addOnField(dailyBeginTime,Calendar.DATE, 1);
		}
		List<PublishLessonCount> list = new ArrayList<PublishLessonCount>();
		if (countList != null && countList.size() > 0) {
			Date tempBegin = null;
			// 初始化每天开始时间
			dailyBeginTime = beginTime;
			// 初始查询每天结束时间
			Date dailyEndTime = DateUtil.addOnField(dailyBeginTime,Calendar.DATE, 1);
			for (PublishLesson tempPublishLesson : countList) {
				// 发布课程开始时间
				tempBegin = tempPublishLesson.getBeginTime();
				// 如果发布开始时间消息大于结束时间，则从新获取每天开始时间
				if (tempBegin.getTime() >= dailyEndTime.getTime()) {
					dailyBeginTime = getDailyBeginTime(dailyBeginTime,tempBegin);
					dailyEndTime = DateUtil.addOnField(dailyBeginTime,Calendar.DATE, 1);
				}
				if (tempBegin.getTime() >= dailyBeginTime.getTime() && tempBegin.getTime() < dailyEndTime.getTime()) {
					if (monthCountMap.containsKey(dailyBeginTime.getTime())) {
						publishLessonCount = monthCountMap.get(dailyBeginTime.getTime());
					
						if (tempPublishLesson.getState() == TalkCommonEnum.PUBLISH_LESSON_STATE_ORDER) {
							publishLessonCount.setOrderCount(publishLessonCount.getOrderCount()+1);
						}
						publishLessonCount.setPublishCount(publishLessonCount.getPublishCount()+1);
						monthCountMap.put(dailyBeginTime.getTime(), publishLessonCount);
					}
				}
			}
		}
		
		for (Map.Entry<Long, PublishLessonCount> entry : monthCountMap.entrySet()) {
			list.add(entry.getValue());
		}

		return list;
	}

	/**
	 * 获取对应时区查询每天开始
	 * @param dailyBeginTime
	 * @param beginTime
	 * @return
	 */
	public Date getDailyBeginTime(Date dailyBeginTime,Date beginTime){
		// 开始时间大于等于开始时间，小于结束数据
		Date dailyEndTime = DateUtil.addOnField(dailyBeginTime,Calendar.DATE, 1);
		if (beginTime.getTime() >= dailyBeginTime.getTime() && beginTime.getTime() < dailyEndTime.getTime()) {
			return dailyBeginTime;
		}else{
			// 开始时间加一天
			dailyBeginTime = dailyEndTime;
			return getDailyBeginTime(dailyBeginTime,beginTime);
		}
	}

	@Override
	public PublishLesson getPublishLessonById(Integer periodId) {
		PublishLesson publishLesson = null;
		if (periodId != null) {
			publishLesson = LessonRedisSource.getPublishLessonCache(periodId);
			if (publishLesson == null) {
				publishLesson = publishLessonMapper.selectByPrimaryKey(periodId);
				if (publishLesson != null) {
					LessonRedisSource.addPublishLessonCache(periodId, publishLesson);
				}
			}
		}
		return publishLesson;
	}
	
}
