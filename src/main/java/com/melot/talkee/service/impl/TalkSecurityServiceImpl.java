package com.melot.talkee.service.impl;


import com.melot.talkee.dao.AdminInfoMapper;
import com.melot.talkee.driver.domain.AdminInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.melot.talkee.driver.domain.EcloudSource;
import com.melot.talkee.driver.domain.UserInfo;
import com.melot.talkee.driver.service.TalkSecurityService;
import com.melot.talkee.driver.service.TalkUserService;
import com.melot.talkee.source.AliEcloudSource;

/**
 * Created by mn on 2017/5/8.
 */
@Service
public class TalkSecurityServiceImpl implements TalkSecurityService{

    private static Logger logger = Logger.getLogger(TalkSecurityServiceImpl.class);
    
    private static final String OSS_PREFIX = "oss_prefix_%s";

    private AliEcloudSource aliEcloudSource;
    
    @Autowired
    private TalkUserService talkUserService;

    @Autowired
    private AdminInfoMapper adminInfoMapper;

	/**
	 * @param aliEcloudSource the aliEcloudSource to set
	 */
	public void setAliEcloudSource(AliEcloudSource aliEcloudSource) {
		this.aliEcloudSource = aliEcloudSource;
	}

	@Override
    public EcloudSource getOssToken(Integer userId) {
		if (userId != null) {

			UserInfo userInfo = talkUserService.getUserInfoByUserId(userId);
			AdminInfo adminInfo= adminInfoMapper.selectAdminInfoByAdminId(userId);

			if (userInfo != null || adminInfo !=null) {
		        String ram_accessKeyId = aliEcloudSource.getAccessKeyId();
		        String ram_accessKeySecret = aliEcloudSource.getAccessKeySecret();
		        String endpoint = aliEcloudSource.getEndpoint();
		        String bucketName = aliEcloudSource.getBucket();
		        String roleArn = aliEcloudSource.getRoleArn();
		        String region = aliEcloudSource.getRegion();
		        String stsApiVersion = aliEcloudSource.getStsApiVersion();
		
		        String roleSessionName = String.format(OSS_PREFIX, userId);
		
		        AssumeRoleResponse roleResponse = null;
		        try {
		            roleResponse = assumeRole(ram_accessKeyId, ram_accessKeySecret, roleArn, roleSessionName,region,stsApiVersion);
		            AssumeRoleResponse.Credentials credentials = roleResponse.getCredentials();
		            if (credentials != null) {
			            EcloudSource ecloudSource = new EcloudSource();
			            ecloudSource.setAccessKeyId(credentials.getAccessKeyId());
			            ecloudSource.setAccessKeySecret(credentials.getAccessKeySecret());
			            ecloudSource.setBucketName(bucketName);
			            ecloudSource.setEndpoint(endpoint);
			            ecloudSource.setExpiration(credentials.getExpiration());
			            ecloudSource.setSecurityToken(credentials.getSecurityToken());
			            ecloudSource.setRegion(region);
			            return ecloudSource;
		            }
		        } catch (ClientException e) {
		            logger.error("ali oss SecurityToken request failed, user_id:" + userId + ",errorMsg:" + e.getLocalizedMessage());
		        }
			}
		}
        return null;
    }

    private AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn, String roleSessionName,String region,String stsApiVersion) throws ClientException {

        try {
            IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);

            DefaultAcsClient client = new DefaultAcsClient(profile);

            final AssumeRoleRequest roleRequest = new AssumeRoleRequest();

            roleRequest.setPolicy(null);
            roleRequest.setRoleSessionName(roleSessionName);
            roleRequest.setRoleArn(roleArn);
            roleRequest.setDurationSeconds(900L);
            roleRequest.setProtocol(ProtocolType.HTTPS);
            roleRequest.setMethod(MethodType.POST);
            roleRequest.setVersion(stsApiVersion);
            final AssumeRoleResponse roleResponse = client.getAcsResponse(roleRequest);

            return roleResponse;
        } catch (ServerException e) {
            throw e;
        } catch (ClientException e) {
            throw e;
        }
    }
}
