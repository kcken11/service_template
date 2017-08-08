package com.melot.talkee.dao;

import java.util.Map;

import com.melot.talkee.driver.domain.ApplyPeriods;
import com.melot.talkee.mybatis.MybatisMapper;

public interface ApplyPeriodsMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(ApplyPeriods record);

    int insertSelective(ApplyPeriods record);

    ApplyPeriods selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(ApplyPeriods record);

    int updateByPrimaryKey(ApplyPeriods record);
    
    int deleteByUserIdAndLinkId(ApplyPeriods record);
}