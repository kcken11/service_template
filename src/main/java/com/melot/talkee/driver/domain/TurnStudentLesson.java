package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class TurnStudentLesson implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer histId;

    private Integer studentId;

    private Integer teacherId;

    private Integer periodId;

    private Date beginTime;

    private Date endTime;

    private String lessonUrl;

    private Integer lessonId;

    private Integer orderUser;

    private Date orderTime;

    private Integer trunVideoState;

    private Integer isStudentComment;  //学生评价

    private Integer isTeacherComment;  //老师评价

    private Integer type;

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

    public String getLessonUrl() {
        return lessonUrl;
    }

    public void setLessonUrl(String lessonUrl) {
        this.lessonUrl = lessonUrl == null ? null : lessonUrl.trim();
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(Integer orderUser) {
        this.orderUser = orderUser;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getTrunVideoState() {
        return trunVideoState;
    }

    public void setTrunVideoState(Integer trunVideoState) {
        this.trunVideoState = trunVideoState;
    }

    public Integer getIsStudentComment() {
        return isStudentComment;
    }

    public void setIsStudentComment(Integer isStudentComment) {
        this.isStudentComment = isStudentComment;
    }

    public Integer getIsTeacherComment() {
        return isTeacherComment;
    }

    public void setIsTeacherComment(Integer isTeacherComment) {
        this.isTeacherComment = isTeacherComment;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}