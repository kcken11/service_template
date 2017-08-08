package com.melot.talkee.driver.domain;

import java.util.Date;

public class UserStudent {
    private Integer userId;

    private String cnNickname;

    private String enNickname;

    private Integer gender;

    private Integer ccId;

    private Integer crId;

    private Integer userType;

    private Integer userLevel;

    private Integer curPeriods;

    private Integer overPeriods;

    private Integer demoPeriods;

    private Integer overDemoPeriods;

    private Date birthday;

    private Date updateTime;

    private Integer tag;

    private String adminDesc;

    private Integer cityId;

    private Integer subLevel;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer timeZone;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCnNickname() {
        return cnNickname;
    }

    public void setCnNickname(String cnNickname) {
        this.cnNickname = cnNickname == null ? null : cnNickname.trim();
    }

    public String getEnNickname() {
        return enNickname;
    }

    public void setEnNickname(String enNickname) {
        this.enNickname = enNickname == null ? null : enNickname.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getCcId() {
        return ccId;
    }

    public void setCcId(Integer ccId) {
        this.ccId = ccId;
    }

    public Integer getCrId() {
        return crId;
    }

    public void setCrId(Integer crId) {
        this.crId = crId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getCurPeriods() {
        return curPeriods;
    }

    public void setCurPeriods(Integer curPeriods) {
        this.curPeriods = curPeriods;
    }

    public Integer getOverPeriods() {
        return overPeriods;
    }

    public void setOverPeriods(Integer overPeriods) {
        this.overPeriods = overPeriods;
    }

    public Integer getDemoPeriods() {
        return demoPeriods;
    }

    public void setDemoPeriods(Integer demoPeriods) {
        this.demoPeriods = demoPeriods;
    }

    public Integer getOverDemoPeriods() {
        return overDemoPeriods;
    }

    public void setOverDemoPeriods(Integer overDemoPeriods) {
        this.overDemoPeriods = overDemoPeriods;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getAdminDesc() {
        return adminDesc;
    }

    public void setAdminDesc(String adminDesc) {
        this.adminDesc = adminDesc == null ? null : adminDesc.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getSubLevel() {
        return subLevel;
    }

    public void setSubLevel(Integer subLevel) {
        this.subLevel = subLevel;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp == null ? null : lastLoginIp.trim();
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }
}