package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.PublishLesson;
import com.melot.talkee.mybatis.MybatisMapper;

public interface PublishLessonMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer periodId);

    int insert(PublishLesson record);

    int insertSelective(PublishLesson record);

    PublishLesson selectByPrimaryKey(Integer periodId);
    
    List<PublishLesson> selectByDaily(Map<String, Object> param);
    
    /**
     * 获取当前时间往后最近开始要上的课程
     * @return
     */
    List<PublishLesson> getLatePublishLesson(Integer state);
    
    /**
     * 验证是否有交集
     * @param paramMap
     * @return
     */
    List<PublishLesson> isIntersect(PublishLesson record);
    
    /**
     * 通过发布课程ID列表查询
     * @param param
     * @return
     */
    List<PublishLesson> selectBySelectiveMap(Map<String, Object> param);
    
    /**
     * 通过用户ID 发布类型，开始时间、结束时间获取每日预约状态分组汇总
     * @param record
     * @return publishDate，state，count
     */
    List<PublishLesson> getMonthCountBySelective(Map<String, Object> param);

    int updateByPrimaryKeySelective(PublishLesson record);

    int updateByPrimaryKey(PublishLesson record);
    
    int deleteBySelectiveMap(Map<String, Object> param);
    
    
	/**
	 * 获取获取课程ID
	 * @return
	 */
	Integer getSeqPublistLesson();
}