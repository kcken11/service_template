package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.LessonLevel;
import com.melot.talkee.driver.domain.Pager;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.List;
import java.util.Map;

public interface LessonLevelMapper extends MybatisMapper {
    int deleteByPrimaryKey(Map<String, Object> param);

    int insert(LessonLevel record);

    int insertSelective(LessonLevel record);

    LessonLevel selectByPrimaryKey(Map<String, Object> param);

    /**
     * 通过父等级查询所有子等级
     *
     * @param parentLevel
     * @return
     */
    List<LessonLevel> selectByParentLevel(Integer parentLevel);

    int updateByPrimaryKeySelective(LessonLevel record);

    int updateByPrimaryKey(LessonLevel record);


    List<LessonLevel> getParentLevel();

}