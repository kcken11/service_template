package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.TurnStudentLesson;
import com.melot.talkee.mybatis.MybatisMapper;

public interface TurnStudentLessonMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(TurnStudentLesson record);

    int insertSelective(TurnStudentLesson record);

    TurnStudentLesson selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(TurnStudentLesson record);

    int updateByPrimaryKey(TurnStudentLesson record);
    
    /**
     * 通过发布课程ID 查询对应预约信息
     * @param periodId
     * @return
     */
    TurnStudentLesson selectByPeriodId(Integer periodId);
    
    /**
     *  通过studentId，stateList[0,1,2] 获取预约记录
     * @param map
     * @return
     */
    List<TurnStudentLesson> selectByUserIdAndStateList(Map<String, Object> map);
}