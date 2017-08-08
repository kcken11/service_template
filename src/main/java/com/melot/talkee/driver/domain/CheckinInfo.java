package com.melot.talkee.driver.domain;

import java.io.Serializable;

public class CheckinInfo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer userId;

    private Integer delayTimes;

    private Integer leaveEarlyTimes;

    private Integer dayOffTimes;

    private Integer freeAskForLeave;

    private Integer periodsAskForLeave;

    private Integer allFreeAskForLeave;

    private Integer userType;

    private Integer normalOver;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDelayTimes() {
        return delayTimes;
    }

    public void setDelayTimes(Integer delayTimes) {
        this.delayTimes = delayTimes;
    }

    public Integer getLeaveEarlyTimes() {
        return leaveEarlyTimes;
    }

    public void setLeaveEarlyTimes(Integer leaveEarlyTimes) {
        this.leaveEarlyTimes = leaveEarlyTimes;
    }

    public Integer getDayOffTimes() {
        return dayOffTimes;
    }

    public void setDayOffTimes(Integer dayOffTimes) {
        this.dayOffTimes = dayOffTimes;
    }

    public Integer getFreeAskForLeave() {
        return freeAskForLeave;
    }

    public void setFreeAskForLeave(Integer freeAskForLeave) {
        this.freeAskForLeave = freeAskForLeave;
    }

    public Integer getPeriodsAskForLeave() {
        return periodsAskForLeave;
    }

    public void setPeriodsAskForLeave(Integer periodsAskForLeave) {
        this.periodsAskForLeave = periodsAskForLeave;
    }

    public Integer getAllFreeAskForLeave() {
        return allFreeAskForLeave;
    }

    public void setAllFreeAskForLeave(Integer allFreeAskForLeave) {
        this.allFreeAskForLeave = allFreeAskForLeave;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getNormalOver() {
        return normalOver;
    }

    public void setNormalOver(Integer normalOver) {
        this.normalOver = normalOver;
    }
}