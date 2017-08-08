package com.melot.talkee.driver.domain;

import java.io.Serializable;

/**
 * Title: PublishLessonCount
 * Description: 发布课程汇总统计
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-13 17:05:53 
 */
public class PublishLessonCount  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long lessonDate;
	
	private Integer publishCount;
	
	private Integer orderCount;
	
	private Integer teacherId;
	
	private Integer publishType;
	
	
	public PublishLessonCount(){
		
	}
	
	public PublishLessonCount(Long lessonDate,Integer publishCount,Integer orderCount,Integer teacherId,Integer publishType){
		this.lessonDate = lessonDate;
		this.publishCount = publishCount;
		this.orderCount = orderCount;
		this.teacherId = teacherId;
		this.publishType = publishType;
	}
	
	/**
	 * @return the lessonDate
	 */
	public Long getLessonDate() {
		return lessonDate;
	}

	/**
	 * @param lessonDate the lessonDate to set
	 */
	public void setLessonDate(Long lessonDate) {
		this.lessonDate = lessonDate;
	}

	/**
	 * @return the publishCount
	 */
	public Integer getPublishCount() {
		return publishCount;
	}

	/**
	 * @param publishCount the publishCount to set
	 */
	public void setPublishCount(Integer publishCount) {
		this.publishCount = publishCount;
	}

	/**
	 * @return the orderCount
	 */
	public Integer getOrderCount() {
		return orderCount;
	}

	/**
	 * @param orderCount the orderCount to set
	 */
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
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
