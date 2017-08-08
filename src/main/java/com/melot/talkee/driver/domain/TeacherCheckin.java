package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class TeacherCheckin implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer histId;

    private Integer teacherId;

    private Integer periodId;

    private Integer abnormalState;

    private String abnormalDesc;

    private Date dtime;

    public TeacherCheckin(){
    	
    }
    
    public TeacherCheckin(Integer teacherId,Integer periodId, Integer abnormalState,String abnormalDesc){
    	this.teacherId = teacherId;
    	this.periodId = periodId;
    	this.abnormalState = abnormalState;
    	this.abnormalDesc = abnormalDesc;
    	this.dtime = new Date();
    }
    
    public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Integer getAbnormalState() {
        return abnormalState;
    }

    public void setAbnormalState(Integer abnormalState) {
        this.abnormalState = abnormalState;
    }

    public String getAbnormalDesc() {
        return abnormalDesc;
    }

    public void setAbnormalDesc(String abnormalDesc) {
        this.abnormalDesc = abnormalDesc == null ? null : abnormalDesc.trim();
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }
}