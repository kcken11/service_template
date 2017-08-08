package com.melot.talkee.driver.domain;

/**
 * Created by mn on 2017/5/15.
 * Email通知类型
 */
public class EmailAdviceTypeEnum {
    /**
     * 预约成功
     **/
    public static final int ORDER_SUCCESS_SINGLE = 11;

    public static final int ORDER_SUCCESS_BATCH = 51;
    /**
     * 上课通知
     **/
    public static final int IN_CLASS = 21;
    /**
     * 老师取消预约课程
     **/
    public static final int TEACHER_CANCEL_ORDER = 31;
    /**
     * 学生取消预约课程
     **/
    public static final int STUDENT_CANCEL_ORDER = 32;

    /**
     * 老师5分钟内取消
     **/
    public static final int TEACHER_CANCEL_ORDER_IN_FIVE_MIN = 33;

    /**
     * 学生5分钟内取消
     **/
    public static final int STUDENT_CANCEL_ORDER_IN_FIVE_MIN = 34;

    /**
     * 用户注册
     **/
    public static final int STUDENT_REGISTER = 41;


}