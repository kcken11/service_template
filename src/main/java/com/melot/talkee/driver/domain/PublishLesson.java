package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class PublishLesson implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer periodId;

    private Date beginTime;

    private Date endTime;

    private Integer state;

    private Integer teacherId;

    private Date publishTime;

    private Integer publishType;

    private Integer maxNum;
    
    private Integer lessonId;
    
    private Integer studentId;
    
    private Integer orderNum;
    
    
    /**
	 * @return the orderNum
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * @return the lessonId
	 */
	public Integer getLessonId() {
		return lessonId;
	}

	/**
	 * @param lessonId the lessonId to set
	 */
	public void setLessonId(Integer lessonId) {
		this.lessonId = lessonId;
	}

	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

	/**
	 * @return the publishType
	 */
	public Integer getPublishType() {
		return publishType;
	}

	/**
	 * @param publishType the publishType to set
	 */
	public void setPublishType(Integer publishType) {
		this.publishType = publishType;
	}
    
    
}