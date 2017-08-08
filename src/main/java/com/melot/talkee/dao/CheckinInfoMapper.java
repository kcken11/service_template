package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.CheckinInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface CheckinInfoMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer userId);

    int insert(CheckinInfo record);

    int insertSelective(CheckinInfo record);

    CheckinInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(CheckinInfo record);

    int updateByPrimaryKey(CheckinInfo record);
}