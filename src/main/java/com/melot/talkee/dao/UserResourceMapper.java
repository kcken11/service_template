package com.melot.talkee.dao;


import java.util.List;

import com.melot.talkee.driver.domain.UserResource;
import com.melot.talkee.mybatis.MybatisMapper;
import org.apache.ibatis.annotations.Param;

public interface UserResourceMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer relId);

    int insert(UserResource record);

    int insertSelective(UserResource record);

    UserResource selectByPrimaryKey(Integer relId);
    
    UserResource selectPortraitByUserId(Integer userId);

    UserResource getPortraitByUserIdAndType(Integer userId);

    List<UserResource> selectBySelective(UserResource record);
    
    int updateByPrimaryKeySelective(UserResource record);

    int updateByPrimaryKey(UserResource record);
    
    int getUserResourceId();
}