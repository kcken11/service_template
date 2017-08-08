package com.melot.talkee.driver.domain;

import java.util.Date;

public class AdminInfo {
    private Integer userId;

    private String enNickname;

    private String cnNickname;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer adminId;

    private Date addTime;

    private Integer tag;

    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEnNickname() {
        return enNickname;
    }

    public void setEnNickname(String enNickname) {
        this.enNickname = enNickname == null ? null : enNickname.trim();
    }

    public String getCnNickname() {
        return cnNickname;
    }

    public void setCnNickname(String cnNickname) {
        this.cnNickname = cnNickname == null ? null : cnNickname.trim();
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

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }
}