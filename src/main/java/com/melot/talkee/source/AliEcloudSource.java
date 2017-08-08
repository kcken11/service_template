package com.melot.talkee.source;

/**
 * Title: AliEcloudSource
 * Description: 阿里云资源
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-09 14:33:53 
 */
public class AliEcloudSource {

	private String endpoint;
	private String bucket;
	private String accessKeyId;
	private String accessKeySecret;
	private String roleArn;
	
	private String region;
	private String stsApiVersion;
	
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the stsApiVersion
	 */
	public String getStsApiVersion() {
		return stsApiVersion;
	}
	/**
	 * @param stsApiVersion the stsApiVersion to set
	 */
	public void setStsApiVersion(String stsApiVersion) {
		this.stsApiVersion = stsApiVersion;
	}
	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}
	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	/**
	 * @return the bucket
	 */
	public String getBucket() {
		return bucket;
	}
	/**
	 * @param bucket the bucket to set
	 */
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	/**
	 * @return the accessKeyId
	 */
	public String getAccessKeyId() {
		return accessKeyId;
	}
	/**
	 * @param accessKeyId the accessKeyId to set
	 */
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	/**
	 * @return the accessKeySecret
	 */
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	/**
	 * @param accessKeySecret the accessKeySecret to set
	 */
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	/**
	 * @return the roleArn
	 */
	public String getRoleArn() {
		return roleArn;
	}
	/**
	 * @param roleArn the roleArn to set
	 */
	public void setRoleArn(String roleArn) {
		this.roleArn = roleArn;
	}
	
	
}
