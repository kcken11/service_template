package com.melot.talkee.driver.exception;

/**
 * 自定义异常
 * <p>
 * 在统一异常处理处，如果是捕获的自定义异常，则返回异常信息给前台
 * 抛自定义异常时，必须对异常的消息进行国际化处理
 * </p>
 */
public class TalkException extends RuntimeException {

    /** 序列号 */
    private static final long serialVersionUID = -1644244205636509307L;

    /**
     * 构造函数
     * @param message 错误消息，必须进行国际化处理
     */
    public TalkException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 错误消息，必须进行国际化处理
     * @param cause {@link Throwable}
     */
    public TalkException(String message, Throwable cause) {
        super(message, cause);
    }
}
