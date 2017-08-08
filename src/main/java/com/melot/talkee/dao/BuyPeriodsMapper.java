package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.BuyPeriods;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.List;
import java.util.Map;

public interface BuyPeriodsMapper  extends MybatisMapper{
    int deleteByPrimaryKey(Integer histId);

    int insert(BuyPeriods record);

    int insertSelective(BuyPeriods record);

    BuyPeriods selectByPrimaryKey(Integer histId);

    int updateByPrimaryKeySelective(BuyPeriods record);

    int updateByPrimaryKey(BuyPeriods record);

    List<BuyPeriods> selectByParamByUserIdAndStatus(Map param);

    BuyPeriods selectByOrderId(String orderId);
}