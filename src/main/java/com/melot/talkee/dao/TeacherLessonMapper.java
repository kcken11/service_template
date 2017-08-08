package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.TeacherLesson;
import com.melot.talkee.mybatis.MybatisMapper;

public interface TeacherLessonMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer reiId);

    int insert(TeacherLesson record);

    int insertSelective(TeacherLesson record);

    TeacherLesson selectByPrimaryKey(Integer reiId);

    int updateByPrimaryKeySelective(TeacherLesson record);

    int updateByPrimaryKey(TeacherLesson record);

    void deleteByTeacherId(int teacherId);
}