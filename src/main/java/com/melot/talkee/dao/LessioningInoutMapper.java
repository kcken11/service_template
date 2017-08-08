package com.melot.talkee.dao;

import java.util.Map;

import com.melot.talkee.driver.domain.LessioningInout;
import com.melot.talkee.mybatis.MybatisMapper;

public interface LessioningInoutMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(LessioningInout record);

    int insertSelective(LessioningInout record);

    LessioningInout selectByPrimaryKey(Integer histId);
    
    LessioningInout selectByPeriodAndUserId(Map<String, Object> param);

    int updateByPrimaryKeySelective(LessioningInout record);

    int updateByPrimaryKey(LessioningInout record);

    LessioningInout selectLastestByPeriodAndUserId(Map<String,Object> param);
}