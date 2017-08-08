package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class StudentCheckin implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer histId;

    private Integer studentId;

    private Integer periodId;

    private Integer abnormalState;

    private String abnormalDesc;

    private Integer consPeriods;

    private Date dtime;
    

    public StudentCheckin(){
    	
    }
    
    public StudentCheckin(Integer studentId,Integer periodId, Integer abnormalState,String abnormalDesc){
    	this.studentId = studentId;
    	this.periodId = periodId;
    	this.consPeriods = periodId;
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

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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

    public Integer getConsPeriods() {
        return consPeriods;
    }

    public void setConsPeriods(Integer consPeriods) {
        this.consPeriods = consPeriods;
    }

    public Date getDtime() {
        return dtime;
    }

    public void setDtime(Date dtime) {
        this.dtime = dtime;
    }
}