package com.melot.talkee.dao;

import java.util.HashMap;
import java.util.List;

import com.melot.talkee.driver.domain.Whiteboard;
import com.melot.talkee.mybatis.MybatisMapper;

public interface WhiteboardMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer histId);

    int insert(Whiteboard record);

    int insertSelective(Whiteboard record);

    Whiteboard selectByPrimaryKey(Integer histId);
    
    List<Whiteboard> selectBySelective(Whiteboard record);

    int updateByPrimaryKeySelective(Whiteboard record);

    int updateByPrimaryKey(Whiteboard record);

    Whiteboard selectByParams(HashMap<String, Object> params);

    Integer deletebyPeriodId(Integer periodId);
}