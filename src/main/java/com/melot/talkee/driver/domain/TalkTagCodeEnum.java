package com.melot.talkee.driver.domain;

public class TalkTagCodeEnum {

    /**
     * 处理正确，无错误返回
     */
    public static final String SUCCESS = "00000000";

    /**
     * 模块参数异常
     */
    public static final String MODULE_PARAMETER_EXCEPTION = "00000207";

    /**
     * lesson_id不能为空
     */
    public static final String LESSON_ID_IS_NULL = "40010150";

    /**
     * 用户等级不存在
     */
    public static final String USER_LEVEL_NOT_EXIST = "40010550";

    /**
     * 课程等级不存在
     */
    public static final String LESSON_LEVEL_NOT_EXIST = "40010551";

    /**
     * 课程等级对应课程不存在
     */
    public static final String LEVEL_RELA_LESSON_NOT_EXIST = "40010552";

    /**
     * 课程等级对应课程预约结束
     */
    public static final String LEVEL_RELA_LESSON_ORDER_OVER = "40010553";
    
    /**
     * 课程预约超出最大等级数
     */
    public static final String OUT_OF_MAX_LEVEL = "40010554";

    /**
     * 超出预约时间
     */
    public static final String OUT_ORDER_TIME = "40010554";

    /**
     * 不能预约
     */
    public static final String CAN_NOT_ORDER = "40010555";


    /**
     * 未分配CR
     */
    public static final String UNDISTRIBUTE_CR = "40010556";


    /**
     * 没权限
     */
    public static final String UNAUTHORIZED = "40010557";

    /**
     * 用户不存在
     */
    public static final String USER_NOT_EXIST = "50010104";

    /**
     * 课程不存在
     */
    public static final String LESSON_NOT_EXIST = "50010105";
    /**
     * 无进入课堂记录
     */
    public static final String ENTER_RECORD_NOT_EXIST = "50010201";

    /**视频录制中**/
    public static final String VIDEO_RECORDING ="50010501" ;

    /**视频合成中**/
    public static final String VIDEO_CONVERTING ="50010502" ;
    /**
     * 订单不存在
     **/
    public static final String TRADE_ORDER_NOT_EXIST = "60010101";

}
