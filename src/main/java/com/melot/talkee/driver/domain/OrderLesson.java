package com.melot.talkee.driver.domain;

import java.io.Serializable;
import java.util.Date;

public class OrderLesson  implements Serializable{
	
	private static final long serialVersionUID = 1L;
   
	private Integer histId;

    private Integer studentId;

    private Integer teacherId;

    private Integer periodId;

    private Date beginTime;

    private Date endTime;

    private Integer lessonState;

	private Integer type;

    private String lessonUrl;

    private Integer lessonId;
    
    private Integer orderUser;
    
    private Date orderTime;
	
    /**
     * 1:普通课时 2:公开课课时 3：试听课时 4:调试课
     */
	private Integer publishType;

	private String teacherCommend;
	
	private Integer isDetailComment;
	
	private StudentCheckin studentCheckin;
	
	private TeacherCheckin teacherCheckin;

	private Integer isStudentComment;  //学生评价

	private Integer isTeacherComment;  //老师评价
	
	/**
	 * @return the teacherCheckin
	 */
	public TeacherCheckin getTeacherCheckin() {
		return teacherCheckin;
	}

	/**
	 * @param teacherCheckin the teacherCheckin to set
	 */
	public void setTeacherCheckin(TeacherCheckin teacherCheckin) {
		this.teacherCheckin = teacherCheckin;
	}

	/**
	 * @return the studentCheckin
	 */
	public StudentCheckin getStudentCheckin() {
		return studentCheckin;
	}

	/**
	 * @param studentCheckin the studentCheckin to set
	 */
	public void setStudentCheckin(StudentCheckin studentCheckin) {
		this.studentCheckin = studentCheckin;
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
	 * @return the teacherCommend
	 */
	public String getTeacherCommend() {
		return teacherCommend;
	}

	/**
	 * @param teacherCommend the teacherCommend to set
	 */
	public void setTeacherCommend(String teacherCommend) {
		this.teacherCommend = teacherCommend;
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

	/**
	 * @return the teacherId
	 */
	public Integer getTeacherId() {
		return teacherId;
	}

	/**
	 * @param teacherId the teacherId to set
	 */
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
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

	/**
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the lessonState
	 */
	public Integer getLessonState() {
		return lessonState;
	}

	/**
	 * @param lessonState the lessonState to set
	 */
	public void setLessonState(Integer lessonState) {
		this.lessonState = lessonState;
	}

	/**
	 * @return the lessonUrl
	 */
	public String getLessonUrl() {
		return lessonUrl;
	}

	/**
	 * @param lessonUrl the lessonUrl to set
	 */
	public void setLessonUrl(String lessonUrl) {
		this.lessonUrl = lessonUrl;
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

	/**
	 * @return the publishType
	 * 1:普通课时 2:公开课课时 3：试听课时 4:调试课
	 */
	public Integer getPublishType() {
		return publishType;
	}

	/**
	 * @param publishType the publishType to set
	 * 1:普通课时 2:公开课课时 3：试听课时 4:调试课
	 */
	public void setPublishType(Integer publishType) {
		this.publishType = publishType;
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