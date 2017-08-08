package com.melot.talkee.driver.domain;

import java.util.Date;

public class VersionInfo {
    
    private Integer versionId;

    private Integer versionCode;

    private String versionName;

    private String versionDesc;

    private String versionUrl;

    private Integer versionStatus;

    private Integer versionPlatform;

    private Date updateTime;

    private Date lastAccessTime;
    
    /**
     * 检测结果
     */
    private Integer checkResult;
    
    /**
     * @return the checkResult 1:无需升级 2:可选升级 3:强制升级 4:系统维护
     */
    public Integer getCheckResult() {
        return checkResult;
    }

    
    /**
     * @param checkResult the checkResult to set
     * 1:无需升级 2:可选升级 3:强制升级 4:系统维护
     */
    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? null : versionName.trim();
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc == null ? null : versionDesc.trim();
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl == null ? null : versionUrl.trim();
    }

    public Integer getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Integer versionStatus) {
        this.versionStatus = versionStatus;
    }

    public Integer getVersionPlatform() {
        return versionPlatform;
    }

    public void setVersionPlatform(Integer versionPlatform) {
        this.versionPlatform = versionPlatform;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}