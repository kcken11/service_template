package com.melot.talkee.dao;

import java.util.List;
import java.util.Map;

import com.melot.talkee.driver.domain.ConfigInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface ConfigInfoMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer configId);

    int insert(ConfigInfo record);

    int insertSelective(ConfigInfo record);

    ConfigInfo selectByPrimaryKey(Integer configId);

    int updateByPrimaryKeySelective(ConfigInfo record);

    int updateByPrimaryKey(ConfigInfo record);
    
    /**
     * 通过配置Key，Platform
     * @param param {configKey,platform,status}
     * @return
     */
    List<ConfigInfo> selectByConfigKeyAndPlatform(Map<String, Object> param);
}