package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.StudentCommentTeacher;
import com.melot.talkee.mybatis.MybatisMapper;

public interface StudentCommentTeacherMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(StudentCommentTeacher record);

    int insertSelective(StudentCommentTeacher record);

    StudentCommentTeacher selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(StudentCommentTeacher record);

    int updateByPrimaryKey(StudentCommentTeacher record);
}