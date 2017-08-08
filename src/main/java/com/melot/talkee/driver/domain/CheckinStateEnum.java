package com.melot.talkee.driver.domain;

public class CheckinStateEnum {

	/**
	 * 老师迟到
	 * 迟到时间小于2分钟
	 */
	public static final int TEACHER_LATE = 4;
	public static final String TEACHER_LATE_INFO = "迟到2分钟内";
	
	/**
	 * 老师严重迟到
	 * 迟到时间大于2分钟小于15分钟
	 */
	public static final int TEACHER_SERIOUS_LATE = 5;
	public static final String TEACHER_SERIOUS_LATE_REWARD = "老师严重迟到小于15分钟，提供一节免费课，学生课时加1";
	public static final String TEACHER_SERIOUS_LATE_PUNISH = "老师严重迟到小于15分钟，提供一节免费课，当前有效课时减1";
	
	/**
	 * 老师非常严重迟到
	 * 迟到时间大于15分钟
	 */
	public static final int TEACHER_VERY_SERIOUS_LATE = 7;
	public static final String TEACHER_VERY_SERIOUS_LATE_REWARD = "老师严重迟到大于15分钟，提供一节免费课，学生课时加1";
	public static final String TEACHER_VERY_SERIOUS_LATE_PUNISH = "老师严重迟到大于15分钟，提供一节免费课，当前有效课时减1";
	
}
