package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class ActionLessioning implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer histId;
	
	private Integer periodId;

    private Integer studentId;

    private Integer teacherId;

    private Integer lessonId;

    private Date beginTime;

    private Date endTime;

    private Integer state;

    private Date teacherInTime;

    private Date teacherOutTime;

    private Date studentInTime;

    private Date studentOutTime;
    
    
    public ActionLessioning(){
    	
    }
    
    public ActionLessioning(Integer periodId,Integer studentId,Integer teacherId,Date beginTime,Date endTime){
    	this.periodId = periodId;
    	this.studentId = studentId;
    	this.teacherId = teacherId;
    	this.beginTime = beginTime;
    	this.endTime = endTime;
    }
    
    /**
	 * @return the histId
	 */
	public Integer getHistId() {
		return histId;
	}

	/**
	 * @param histId the histId to set
	 */
	public void setHistId(Integer histId) {
		this.histId = histId;
	}


    /**
	 * @return the periodId
	 */
	public Integer getPeriodId() {
		return periodId;
	}

	/**
	 * @param periodId the periodId to set
	 */
	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
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

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getTeacherInTime() {
        return teacherInTime;
    }

    public void setTeacherInTime(Date teacherInTime) {
        this.teacherInTime = teacherInTime;
    }

    public Date getTeacherOutTime() {
        return teacherOutTime;
    }

    public void setTeacherOutTime(Date teacherOutTime) {
        this.teacherOutTime = teacherOutTime;
    }

    public Date getStudentInTime() {
        return studentInTime;
    }

    public void setStudentInTime(Date studentInTime) {
        this.studentInTime = studentInTime;
    }

    public Date getStudentOutTime() {
        return studentOutTime;
    }

    public void setStudentOutTime(Date studentOutTime) {
        this.studentOutTime = studentOutTime;
    }
}