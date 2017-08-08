package com.melot.talkee.dao;

import java.util.Map;

import com.melot.talkee.driver.domain.SmsConfig;
import com.melot.talkee.mybatis.MybatisMapper;

public interface SmsConfigMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer templateId);

    int insert(SmsConfig record);

    int insertSelective(SmsConfig record);

    SmsConfig selectByPrimaryKey(Integer templateId);

    int updateByPrimaryKeySelective(SmsConfig record);

    int updateByPrimaryKey(SmsConfig record);
    
    SmsConfig selectBySmsTypeAndUserType(Map<String, Object> map);
}