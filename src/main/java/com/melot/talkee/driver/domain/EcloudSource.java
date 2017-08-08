package com.melot.talkee.driver.domain;

import java.io.Serializable;


public class EcloudSource implements Serializable{

	private static final long serialVersionUID = 1L;

	 private String bucketName;
	 private String accessKeyId;
	 private String accessKeySecret;
	 private String expiration;
	 private String securityToken;
	 private String endpoint;
	 private String region;
	 
	 
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
	 * @return the bucketName
	 */
	public String getBucketName() {
		return bucketName;
	}
	/**
	 * @param bucketName the bucketName to set
	 */
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
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
	 * @return the expiration
	 */
	public String getExpiration() {
		return expiration;
	}
	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	/**
	 * @return the securityToken
	 */
	public String getSecurityToken() {
		return securityToken;
	}
	/**
	 * @param securityToken the securityToken to set
	 */
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
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
	
}
