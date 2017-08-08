package com.melot.talkee.dao;

import java.util.List;

import com.melot.talkee.driver.domain.TeacherDetailComment;
import com.melot.talkee.mybatis.MybatisMapper;

public interface TeacherDetailCommentMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(TeacherDetailComment record);

    int insertSelective(TeacherDetailComment record);

    TeacherDetailComment selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(TeacherDetailComment record);

    int updateByPrimaryKey(TeacherDetailComment record);
    
    List<TeacherDetailComment> selectBySelective(TeacherDetailComment record);
}