package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.StudentCheckin;
import com.melot.talkee.mybatis.MybatisMapper;

public interface StudentCheckinMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(StudentCheckin record);

    int insertSelective(StudentCheckin record);

    StudentCheckin selectByPrimaryKey(Integer histId);
    
    StudentCheckin selectByPeriodAndStudentId(Map<String, Object> param);
    
    List<StudentCheckin> selectBySelective(StudentCheckin record);

    int updateByPrimaryKeySelective(StudentCheckin record);

    int updateByPrimaryKey(StudentCheckin record);
}