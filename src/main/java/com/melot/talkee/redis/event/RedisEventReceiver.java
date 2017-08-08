package com.melot.talkee.redis.event;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.melot.common.melot_jedis.JedisWrapper;

/**
 * Title: RedisEventReceiver
 * <p>
 * Description: 处理订阅事件
 * </p>
 * @author  姚国平<a href="mailto:guoping.yao@melot.cn">
 * @version V1.0
 * @since 2016-4-29 上午9:30:44 
 */
public class RedisEventReceiver extends Thread{

    private static Logger logger = Logger.getLogger(RedisEventReceiver.class);
    
    /** 执行redis事件  */
    private RedisEventHandler redisEventHandler;
    
    /** 是否是通配通道订阅, 默认单一通道 */
    private boolean isPsubscribe = false;
    
    private String subscribePatter;
    
    /** jedis客户端连接池 */
    private JedisWrapper jedisWrapper;
    
    public RedisEventReceiver(RedisEventHandler redisEventHandler, String subscribePatter, JedisWrapper jedisWrapper) {
        this(redisEventHandler, subscribePatter, jedisWrapper, false);
    } 
    
    public RedisEventReceiver(RedisEventHandler redisEventHandler,
           String subscribePatter, 
           JedisWrapper jedisWrapper, 
           boolean isPsubscribe) {
        this.redisEventHandler = redisEventHandler;
        this.isPsubscribe = isPsubscribe;
        this.subscribePatter = subscribePatter;
        this.jedisWrapper = jedisWrapper;
    }

    /**
     *  完成初始化,启动 
     */
    public void init(){
        if(jedisWrapper == null){
            logger.error("jedisPool is null, work finished!");
        }
        this.start();
    }
    
    @Override
    public void run() {
        while (true) {
            boolean errHappend = false;
            Jedis jedis = null;
            Exception t = null;
            try {
                jedis = jedisWrapper.getJedis();
                if(jedis != null){
	                if(isPsubscribe){
	                    jedis.psubscribe(new Subscriber(), subscribePatter);
	                }else{
	                    jedis.subscribe(new Subscriber(), subscribePatter);
	                }
                }
            } catch (Exception e) {
                errHappend = true;
                t = e;
                logger.error("execute redis error:", e);
            } finally {
                if (jedis != null) {
                    if(errHappend){
                    	jedisWrapper.returnBrokenJedis(jedis, t);
                    }else{
                    	jedisWrapper.returnJedis(jedis);
                    }
                }
            }
        }
    }
    
    class Subscriber extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            redisEventHandler.handler(message);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            redisEventHandler.handler(message);
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            
        }

        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
        }

        @Override
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
        }
    }
}
