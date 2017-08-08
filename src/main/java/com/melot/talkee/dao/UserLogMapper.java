package com.melot.talkee.dao;

import com.melot.talkee.dao.domain.UserLog;
import com.melot.talkee.mybatis.MybatisMapper;

public interface UserLogMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(UserLog record);

    int insertSelective(UserLog record);

    UserLog selectByPrimaryKey(Integer histId);
    
    UserLog  getlastBySelective(UserLog record);

    int updateByPrimaryKeySelective(UserLog record);

    int updateByPrimaryKey(UserLog record);
    
}