package com.melot.talkee.dao;

import java.util.List;

import com.melot.talkee.driver.domain.Courseware;
import com.melot.talkee.mybatis.MybatisMapper;

public interface CoursewareMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer coswId);

    int insert(Courseware record);

    int insertSelective(Courseware record);

    Courseware selectByPrimaryKey(Integer coswId);

    int updateByPrimaryKeySelective(Courseware record);

    int updateByPrimaryKey(Courseware record);
    
    List<Courseware> selectBySelective(Courseware record);
}