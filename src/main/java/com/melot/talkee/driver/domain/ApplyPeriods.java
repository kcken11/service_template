package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class ApplyPeriods implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer histId;

    private Integer ccId;

    private Integer userId;

    private Integer state;

    private Date applyTime;

    private Integer caId;

    private Date checkTime;

    private String applyDescribe;

    private String chackDescribe;

    private Integer periodsType;

    private Integer applyPeriods;

    private Integer linkid;
    
    private Integer userType;

	public ApplyPeriods(){
    	
    }
    
    /**
	 * @return the userType
	 */
	public Integer getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

    public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    public Integer getCcId() {
        return ccId;
    }

    public void setCcId(Integer ccId) {
        this.ccId = ccId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getCaId() {
        return caId;
    }

    public void setCaId(Integer caId) {
        this.caId = caId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getApplyDescribe() {
        return applyDescribe;
    }

    public void setApplyDescribe(String applyDescribe) {
        this.applyDescribe = applyDescribe == null ? null : applyDescribe.trim();
    }

    public String getChackDescribe() {
        return chackDescribe;
    }

    public void setChackDescribe(String chackDescribe) {
        this.chackDescribe = chackDescribe == null ? null : chackDescribe.trim();
    }

    public Integer getPeriodsType() {
        return periodsType;
    }

    public void setPeriodsType(Integer periodsType) {
        this.periodsType = periodsType;
    }

    public Integer getApplyPeriods() {
        return applyPeriods;
    }

    public void setApplyPeriods(Integer applyPeriods) {
        this.applyPeriods = applyPeriods;
    }

    public Integer getLinkid() {
        return linkid;
    }

    public void setLinkid(Integer linkid) {
        this.linkid = linkid;
    }
}