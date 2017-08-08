package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class UserInfo  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer   userId       ;//  integer NOT NULL,
	private Integer   channelId    ;//  integer,
	private Date   registerTime ;//  timestamp WITHOUT TIME ZONE,
	private String   registerIp   ;//  text,
	private Integer   terminal      ;//  integer,
	private String   phoneNum     ;//  text,
	private String   email         ;//  text,
	private String   password      ;// text,
	private Integer   port          ;//  integer,
	private Integer   channelType  ;//  integer,
	private String   relatitionship;//  text,
	private String   describe      ;//  text,
	private Integer   accountType  ;//  integer,
	private String loginName;
	private String token;
	private String portrait;
	public Integer supply;
	
	/**
	 * @return the supply
	 */
	public Integer getSupply() {
		return supply;
	}
	/**
	 * @param supply the supply to set
	 */
	public void setSupply(Integer supply) {
		this.supply = supply;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the portrait
	 */
	public String getPortrait() {
		return portrait;
	}
	/**
	 * @param portrait the portrait to set
	 */
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return the channelId
	 */
	public Integer getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the registerTime
	 */
	public Date getRegisterTime() {
		return registerTime;
	}
	/**
	 * @param registerTime the registerTime to set
	 */
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	/**
	 * @return the registerIp
	 */
	public String getRegisterIp() {
		return registerIp;
	}
	/**
	 * @param registerIp the registerIp to set
	 */
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	/**
	 * @return the terminal
	 */
	public Integer getTerminal() {
		return terminal;
	}
	/**
	 * @param terminal the terminal to set
	 */
	public void setTerminal(Integer terminal) {
		this.terminal = terminal;
	}
	/**
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * @return the channelType
	 */
	public Integer getChannelType() {
		return channelType;
	}
	/**
	 * @param channelType the channelType to set
	 */
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}
	/**
	 * @return the relatitionship
	 */
	public String getRelatitionship() {
		return relatitionship;
	}
	/**
	 * @param relatitionship the relatitionship to set
	 */
	public void setRelatitionship(String relatitionship) {
		this.relatitionship = relatitionship;
	}
	/**
	 * @return the describe
	 */
	public String getDescribe() {
		return describe;
	}
	/**
	 * @param describe the describe to set
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	/**
	 * @return the accountType
	 */
	public Integer getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	
	
}                  
