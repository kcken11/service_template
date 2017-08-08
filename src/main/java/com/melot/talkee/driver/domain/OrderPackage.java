package com.melot.talkee.driver.domain;

import java.util.Date;

public class OrderPackage {
    private Integer packageId;

    private String packageName;

    private Integer periords;

    private Integer publicLesson;

    private Integer state;

    private Integer oldAmount;

    private Integer curAmount;

    private Date beginTime;

    private Date endTime;

    private Integer confTime;

    private Integer vaildDays;

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    public Integer getPeriords() {
        return periords;
    }

    public void setPeriords(Integer periords) {
        this.periords = periords;
    }

    public Integer getPublicLesson() {
        return publicLesson;
    }

    public void setPublicLesson(Integer publicLesson) {
        this.publicLesson = publicLesson;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(Integer oldAmount) {
        this.oldAmount = oldAmount;
    }

    public Integer getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(Integer curAmount) {
        this.curAmount = curAmount;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getConfTime() {
        return confTime;
    }

    public void setConfTime(Integer confTime) {
        this.confTime = confTime;
    }

    public Integer getVaildDays() {
        return vaildDays;
    }

    public void setVaildDays(Integer vaildDays) {
        this.vaildDays = vaildDays;
    }
}