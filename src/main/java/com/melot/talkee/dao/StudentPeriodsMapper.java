package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.StudentPeriods;
import com.melot.talkee.mybatis.MybatisMapper;

public interface StudentPeriodsMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer resId);

    int insert(StudentPeriods record);

    int insertSelective(StudentPeriods record);

    StudentPeriods selectByPrimaryKey(Integer resId);

    int updateByPrimaryKeySelective(StudentPeriods record);

    int updateByPrimaryKey(StudentPeriods record);
    
    List<StudentPeriods> selectBySelective(StudentPeriods record);
    
    StudentPeriods selectByUserIdAndPeriodType(Map<String, Object> param);

    /**
     * 根据userId,lessonType查看该用户已上课程数量
     * @param param
     * @return
     */
    int selectCountByUserIdAndLessonType(Map<String,Object> param);
}