package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.EmailConfig;
import com.melot.talkee.mybatis.MybatisMapper;

public interface EmailConfigMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer templateId);

    int insert(EmailConfig record);

    int insertSelective(EmailConfig record);

    EmailConfig selectByPrimaryKey(Integer templateId);

    int updateByPrimaryKeySelective(EmailConfig record);

    int updateByPrimaryKey(EmailConfig record);

    EmailConfig selectByParam(EmailConfig emailConfig);
}