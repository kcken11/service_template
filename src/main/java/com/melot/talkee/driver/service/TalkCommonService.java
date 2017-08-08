package com.melot.talkee.driver.service;

import com.melot.talkee.driver.domain.Channel;
import com.melot.talkee.driver.domain.ConfigInfo;
import com.melot.talkee.driver.domain.SmsConfig;
import com.melot.talkee.driver.domain.VersionInfo;

import java.util.List;

/**
 * Title: TalkCommonServer
 * Description: 公共服务接口
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-05-26 11:09:53 
 */
public interface TalkCommonService {

	/**
	 * 获取短信文本配置接口
	 * @param smsType
	 * @param userType
	 * @param phoneNum
	 * @return
	 */
	SmsConfig getSmsConfig(Integer smsType,Integer userType);
	
	 /**
     * 获取渠道号列表
     * @return
     */
	List<Channel> getChannelList(Integer type);
	
	 /**
	  * 获取最新版本
	  * @param version
	  * @param platform
	  * @return
	  */
    VersionInfo getLatestVersion(String version,Integer platform);
    
    /**
     * 获取当前版本信息
     * @param version
     * @param platform
     * @return
     */
    VersionInfo getCurrentVersion(String version,Integer platform);
    
    /**
     * 获取配置信息
     * @param configKey
     * @param platform
     * @return
     */
    List<ConfigInfo> getConfigInfo(String configKey,Integer platform);
}
