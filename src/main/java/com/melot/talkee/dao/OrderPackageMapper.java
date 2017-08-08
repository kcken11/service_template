package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.OrderPackage;
import com.melot.talkee.mybatis.MybatisMapper;

public interface OrderPackageMapper extends MybatisMapper {
    int deleteByPrimaryKey(Integer packageId);

    int insert(OrderPackage record);

    int insertSelective(OrderPackage record);

    OrderPackage selectByPrimaryKey(Integer packageId);

    int updateByPrimaryKeySelective(OrderPackage record);

    int updateByPrimaryKey(OrderPackage record);
}