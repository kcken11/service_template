package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.AdminInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface AdminInfoMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(AdminInfo record);

    int insertSelective(AdminInfo record);

    AdminInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(AdminInfo record);

    int updateByPrimaryKey(AdminInfo record);

    AdminInfo selectByAdminId(int adminId);

    AdminInfo selectAdminInfoByAdminId(int adminId);

}