package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.TeacherInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface UserTeacherMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer teacherId);

    int insert(TeacherInfo record);

    int insertSelective(TeacherInfo record);

    TeacherInfo selectByPrimaryKey(Integer teacherId);

    int updateByPrimaryKeySelective(TeacherInfo record);

    int updateByPrimaryKey(TeacherInfo record);
}