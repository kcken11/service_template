package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class Whiteboard implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Integer histId;

    private Integer userId;

    private Integer periodId;

    private Integer lessonId;

    private Integer lessonVersion;

    private Date createTime;

    private Integer segment;

    private String msgData;

    public Whiteboard(){
        
    }
    
    public Whiteboard(Integer userId,Integer periodId,Integer segment){
        this.userId = userId;
        this.periodId = periodId;
        this.segment = segment;
    }
    
    public Whiteboard(Integer userId,Integer periodId,Integer segment,String msgData){
        this.userId = userId;
        this.periodId = periodId;
        this.segment = segment;
        this.msgData = msgData;
        this.createTime = new Date();
    }
    
    public Integer getHistId() {
        return histId;
    }

    public void setHistId(Integer histId) {
        this.histId = histId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getLessonVersion() {
        return lessonVersion;
    }

    public void setLessonVersion(Integer lessonVersion) {
        this.lessonVersion = lessonVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgData) {
        this.msgData = msgData == null ? null : msgData.trim();
    }
}