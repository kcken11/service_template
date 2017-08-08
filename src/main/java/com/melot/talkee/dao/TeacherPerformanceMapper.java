package com.melot.talkee.dao;


import java.util.Map;

import com.melot.talkee.driver.domain.TeacherPerformance;
import com.melot.talkee.driver.domain.TeacherOverPeriodCount;
import com.melot.talkee.mybatis.MybatisMapper;

public interface TeacherPerformanceMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer histId);

    int insert(TeacherPerformance record);

    int insertSelective(TeacherPerformance record);

    TeacherPerformance selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(TeacherPerformance record);

    int updateByPrimaryKey(TeacherPerformance record);

    TeacherPerformance selectByPeriodId(Integer histId);

    TeacherOverPeriodCount selectSum(Integer teacherId);
    
    TeacherOverPeriodCount selectSumByTeacherAndDtime(Map<String, Object> param);
    
    TeacherPerformance getLateByMap(Map<String, Object> param);
    
}