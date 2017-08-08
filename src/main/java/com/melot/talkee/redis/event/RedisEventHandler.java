package com.melot.talkee.redis.event;

/**
 * Title: RedisEventHandler
 * <p>
 * Description: TODO
 * </p>
 * @author  姚国平<a href="mailto:guoping.yao@melot.cn">
 * @version V1.0
 * @since 2016-4-29 上午9:28:11 
 */
public interface RedisEventHandler {

    void handler(String message);
}
