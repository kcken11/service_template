package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.Lesson;
import com.melot.talkee.driver.domain.Pager;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.List;
import java.util.Map;

public interface LessonMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer lessonId);

    int insert(Lesson record);

    int insertSelective(Lesson record);

    Lesson selectByPrimaryKey(Integer lessonId);

    Lesson selectByLevel(Map<String, Object> param);

    List<Lesson> selectSelective(Lesson record);

    int updateByPrimaryKeySelective(Lesson record);

    int updateByPrimaryKey(Lesson record);

    /**
     * 获取当前课程等级剩余可预约数
     *
     * @param param lessonLevel、subLevel、lessonType、status
     * @return
     */
    int selectOverplusCount(Map<String, Object> param);

    List<Lesson> findPager(Pager<Lesson> pager);
}