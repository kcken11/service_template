package com.melot.talkee.dao;

import java.util.Map;

import com.melot.talkee.driver.domain.TeacherCheckin;
import com.melot.talkee.mybatis.MybatisMapper;

public interface TeacherCheckinMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(TeacherCheckin record);

    int insertSelective(TeacherCheckin record);

    TeacherCheckin selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(TeacherCheckin record);

    int updateByPrimaryKey(TeacherCheckin record);
    
    TeacherCheckin selectByPeriodAndTeacherId(Map<String, Object> param);
    
    
}