package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.Portrait;
import com.melot.talkee.mybatis.MybatisMapper;

public interface PortraitMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer portraitId);

    int insert(Portrait record);

    int insertSelective(Portrait record);

    Portrait selectByPrimaryKey(Integer portraitId);

    int updateByPrimaryKeySelective(Portrait record);

    int updateByPrimaryKey(Portrait record);
    
    int getPortraitId();
}