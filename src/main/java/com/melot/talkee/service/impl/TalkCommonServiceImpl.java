package com.melot.talkee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.dao.ChannelMapper;
import com.melot.talkee.dao.ConfigInfoMapper;
import com.melot.talkee.dao.VersionInfoMapper;
import com.melot.talkee.driver.domain.Channel;
import com.melot.talkee.driver.domain.ConfigInfo;
import com.melot.talkee.driver.domain.VersionInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melot.talkee.dao.SmsConfigMapper;
import com.melot.talkee.driver.domain.SmsConfig;
import com.melot.talkee.driver.service.TalkCommonService;

@Service
public class TalkCommonServiceImpl implements TalkCommonService {

   private static final Logger LOGGER = Logger.getLogger(TalkCommonServiceImpl.class);

    @Autowired
    private SmsConfigMapper smsConfigMapper;

    @Autowired
    private ChannelMapper channelMapper;
    
    @Autowired
    private VersionInfoMapper versionInfoMapper;

    @Autowired
    private ConfigInfoMapper configInfoMapper;
    
    /* (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkCommonServer#getSmsConfig(java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @Override
    public SmsConfig getSmsConfig(Integer smsType, Integer userType) {
        if (smsType != null && userType != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("adviceType", smsType);
            param.put("adviceUserType", userType);
            return smsConfigMapper.selectBySmsTypeAndUserType(param);
        }
        return null;
    }

    @Override
    public List<Channel> getChannelList(Integer type) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            if (type != null) {
                param.put("channelType", type);
            }
        	return channelMapper.selectChannels(param);
        } catch (Exception e) {
        	LOGGER.error("com.melot.talkee.service.impl.TalkCommonServiceImpl.getChannelList is error",e);
        }
        return null;
    }

    /** (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkCommonService#getLatestVersion(java.lang.String, java.lang.Integer)
     */
    @Override
    public VersionInfo getLatestVersion(String version, Integer platform) {
        if (platform != null) {
            VersionInfo versionInfo = versionInfoMapper.selectLatestVersion(platform);
            if (versionInfo != null) {
                if (StringUtils.isNotBlank(version)) {
                    VersionInfo currentVersion = getCurrentVersion(version,platform);
                    if (currentVersion != null) {
                        int currentStatus = currentVersion.getVersionStatus();
                        // 版本未启用、或标记强制升级
                        if (currentStatus == 0 || currentStatus == 2 ) {
                            versionInfo.setCheckResult(3);
                        // 系统维护
                        }else if (currentStatus == 3) {
                            versionInfo.setCheckResult(4);
                        }else {
                            // 当前最新版本
                            if (versionInfo.getVersionId().intValue() == currentVersion.getVersionId().intValue()) {
                                versionInfo.setCheckResult(1);
                            }else{
                                // 可选升级
                                versionInfo.setCheckResult(2);
                            }
                        }
                    }
                }else{
                    versionInfo.setCheckResult(3);
                }
                String versionUrl = versionInfo.getVersionUrl();
                if (StringUtils.isNotBlank(versionUrl)) {
                    String yunUrl = "";
                    if(versionUrl.startsWith("aliyun")){
                        yunUrl = MelotBeanFactory.getBean("aliyun", String.class);
                    }
                    versionUrl = yunUrl+"/"+versionUrl;
                    versionInfo.setVersionUrl(versionUrl);
                }
                return versionInfo;
            }
        }
        return null;
    }

    /** (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkCommonService#getCurrentVersion(java.lang.String, java.lang.Integer)
     */
    @Override
    public VersionInfo getCurrentVersion(String version, Integer platform) {
        if (StringUtils.isNotBlank(version) && platform != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("versionName", version);
            param.put("versionPlatform", platform);
            
            return versionInfoMapper.selectByCodeAndPlatform(param);
        }
        return null;
    }

    /** (non-Javadoc)
     * @see com.melot.talkee.driver.service.TalkCommonService#getConfigInfo(java.lang.String, java.lang.Integer)
     */
    @Override
    public List<ConfigInfo> getConfigInfo(String configKey, Integer platform) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (platform != null) {
            param.put("platform", String.valueOf(platform));
        }
        if (StringUtils.isNotBlank(configKey)) {
            param.put("configKey", configKey);
        }
        param.put("status", 1);
        return  configInfoMapper.selectByConfigKeyAndPlatform(param);
    }
}
