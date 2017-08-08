package com.melot.talkee.dao;

import java.util.Map;

import com.melot.talkee.driver.domain.VersionInfo;
import com.melot.talkee.mybatis.MybatisMapper;

public interface VersionInfoMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer versionId);

    int insert(VersionInfo record);

    int insertSelective(VersionInfo record);

    VersionInfo selectByPrimaryKey(Integer versionId);

    int updateByPrimaryKeySelective(VersionInfo record);

    int updateByPrimaryKey(VersionInfo record);
    
    /**
     * 获取当前版本信息
     * @param param {versionCode,versionPlatform}
     * @return
     */
    VersionInfo  selectByCodeAndPlatform(Map<String, Object> param);
    
    /**
     * 获取平台最新版本
     * @param versionPlatform
     * @return
     */
    VersionInfo  selectLatestVersion(Integer versionPlatform);
    
    /**
     * 修改当前版本最后访问时间
     * @param param {versionCode,versionPlatform}
     * @return
     */
    int updateLastAccessTime(Map<String, Object> param);
}