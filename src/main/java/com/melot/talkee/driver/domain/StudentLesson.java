package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class StudentLesson implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Integer histId;

    private Integer studentId;

    private Integer teacherId;

    private Integer consPeriods;

    private Integer consType;

    private Integer periodId;

    private Date beginTime;

    private Date endTime;

    private Integer lessonState;

    private Integer type; //是否是学生请假 1：学生请假 0：其他

    private String teacherCommend;

    private Integer addPoint;

    private String lessonUrl;

    private Integer lessonId;
    
    private Integer orderUser;
    
    private Date orderTime;
    
    private Integer isDetailComment;

	private Integer isStudentComment;  //学生评价

	private Integer isTeacherComment;  //老师评价
    
    private String teacherUrl;
    
    private Integer reviewTimes;
    
    private Integer cancleUser;
    
    private String cancleReason;
    
    private String studentUrl;
    
    private Date cancleTime;
    /* 声网channel(频道号) */
	private String channelId;
	/* 声网channelKey(频道密码) */
	private String channelKey;
   /**学生录制开始时间 精确到毫秒*/
	private String studentStartTime;

	private Integer studentDuration;

	private String teacherStartTime;

	private Integer teacherDuration;

	public String getStudentStartTime() {
		return studentStartTime;
	}

	public void setStudentStartTime(String studentStartTime) {
		this.studentStartTime = studentStartTime;
	}

	public Integer getStudentDuration() {
		return studentDuration;
	}

	public void setStudentDuration(Integer studentDuration) {
		this.studentDuration = studentDuration;
	}

	public String getTeacherStartTime() {
		return teacherStartTime;
	}

	public void setTeacherStartTime(String teacherStartTime) {
		this.teacherStartTime = teacherStartTime;
	}

	public Integer getTeacherDuration() {
		return teacherDuration;
	}

	public void setTeacherDuration(Integer teacherDuration) {
		this.teacherDuration = teacherDuration;
	}

	/**
	 * @return the cancleTime
	 */
	public Date getCancleTime() {
		return cancleTime;
	}

	/**
	 * @param cancleTime the cancleTime to set
	 */
	public void setCancleTime(Date cancleTime) {
		this.cancleTime = cancleTime;
	}

	/**
	 * @return the isDetailComment
	 */
	public Integer getIsDetailComment() {
		return isDetailComment;
	}

	/**
	 * @param isDetailComment the isDetailComment to set
	 */
	public void setIsDetailComment(Integer isDetailComment) {
		this.isDetailComment = isDetailComment;
	}

	/**
	 * @return the teacherUrl
	 */
	public String getTeacherUrl() {
		return teacherUrl;
	}

	/**
	 * @param teacherUrl the teacherUrl to set
	 */
	public void setTeacherUrl(String teacherUrl) {
		this.teacherUrl = teacherUrl;
	}

	/**
	 * @return the reviewTimes
	 */
	public Integer getReviewTimes() {
		return reviewTimes;
	}

	/**
	 * @param reviewTimes the reviewTimes to set
	 */
	public void setReviewTimes(Integer reviewTimes) {
		this.reviewTimes = reviewTimes;
	}

	/**
	 * @return the cancleUser
	 */
	public Integer getCancleUser() {
		return cancleUser;
	}

	/**
	 * @param cancleUser the cancleUser to set
	 */
	public void setCancleUser(Integer cancleUser) {
		this.cancleUser = cancleUser;
	}

	/**
	 * @return the cancleReason
	 */
	public String getCancleReason() {
		return cancleReason;
	}

	/**
	 * @param cancleReason the cancleReason to set
	 */
	public void setCancleReason(String cancleReason) {
		this.cancleReason = cancleReason;
	}

	/**
	 * @return the studentUrl
	 */
	public String getStudentUrl() {
		return studentUrl;
	}

	/**
	 * @param studentUrl the studentUrl to set
	 */
	public void setStudentUrl(String studentUrl) {
		this.studentUrl = studentUrl;
	}

	/**
	 * @return the orderUser
	 */
	public Integer getOrderUser() {
		return orderUser;
	}

	/**
	 * @param orderUser the orderUser to set
	 */
	public void setOrderUser(Integer orderUser) {
		this.orderUser = orderUser;
	}

	/**
	 * @return the orderTime
	 */
	public Date getOrderTime() {
		return orderTime;
	}

	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getConsPeriods() {
        return consPeriods;
    }

    public void setConsPeriods(Integer consPeriods) {
        this.consPeriods = consPeriods;
    }

    public Integer getConsType() {
        return consType;
    }

    public void setConsType(Integer consType) {
        this.consType = consType;
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

    public Integer getLessonState() {
        return lessonState;
    }

    public void setLessonState(Integer lessonState) {
        this.lessonState = lessonState;
    }

    public String getTeacherCommend() {
        return teacherCommend;
    }

    public void setTeacherCommend(String teacherCommend) {
        this.teacherCommend = teacherCommend == null ? null : teacherCommend.trim();
    }

    public Integer getAddPoint() {
        return addPoint;
    }

    public void setAddPoint(Integer addPoint) {
        this.addPoint = addPoint;
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

	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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