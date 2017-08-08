package com.melot.talkee.dao;

import com.melot.talkee.driver.domain.Channel;
import com.melot.talkee.mybatis.MybatisMapper;

import java.util.List;
import java.util.Map;

public interface ChannelMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer channelId);

    int insert(Channel record);

    int insertSelective(Channel record);

    Channel selectByPrimaryKey(Integer channelId);

    int updateByPrimaryKeySelective(Channel record);

    int updateByPrimaryKey(Channel record);

    List<Channel> selectChannels(Map<String, Object> map);
}