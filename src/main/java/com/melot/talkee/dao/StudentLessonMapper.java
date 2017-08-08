package com.melot.talkee.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.OrderLesson;
import com.melot.talkee.driver.domain.Pager;
import com.melot.talkee.driver.domain.StudentLesson;
import com.melot.talkee.mybatis.MybatisMapper;

public interface StudentLessonMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(StudentLesson record);

    int insertSelective(StudentLesson record);

    StudentLesson selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(StudentLesson record);

    int updateByPrimaryKey(StudentLesson record);
    
    /**
     * 通过发布课程ID 查询对应预约信息
     * @param periodId
     * @return
     */
    List<OrderLesson> selectByPeriodId(Integer periodId);
    
    
    /**
     * 通过发布课程ID 查询对应预约信息
     * @param map
     * @return
     */
    StudentLesson selectByUserIdAndPeriodId(Map<String, Object> map);
    
    /**
     * 通过studentId，stateList[0,1,2,3] 获取最后一条预约记录
     * @param map
     * @return
     */
    StudentLesson getLastOrderLesson(Map<String, Object> map);
    
    
    /**
     * 通过studentId，stateList[0,1,2,3] 获取最近要上课
     * @param map
     * @return
     */
    OrderLesson getNextOrderLesson(Map<String, Object> map);
    
    /**
     * 根据类型状态，获取下一条课程
     * @param map
     * @return
     */
    StudentLesson getNextByUserIdAndType(Map<String, Object> map);
    
    
    /**
     *  通过studentId，stateList[0,1,2] 获取预约记录
     * @param map
     * @return
     */
    List<StudentLesson> selectByUserIdAndStateList(Map<String, Object> map);
    
    
    List<OrderLesson> getOrderLesson(Map<String, Object> map);
    
    /**
     *  通过studentId，stateList[0,1,2] 获取预约记录
     * @param pager
     * @return
     */
    List<OrderLesson> findPager(Pager<OrderLesson> pager);

    List<OrderLesson> findAwaitPager(Pager<OrderLesson> pager);

    List<OrderLesson> findOverPager(Pager<OrderLesson> pager);

    List<OrderLesson> findLeavePager(Pager<OrderLesson> pager);
    
    /**
     * 获取非lessonState状态下开始结束时间范围内数据
     * @param map
     * @return
     */
    List<OrderLesson> isIntersect(Map<String, Object> map);

    /**
     * 获取lesson 信息
     * @param periodId
     * @return
     */
    StudentLesson getOrderLessonByPeriodId(Integer periodId);

    HashMap<String,Object> selectLessonRecordByPeriodId(Integer periodId);
}