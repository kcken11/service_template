package com.melot.talkee.redis.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melot.common.melot_jedis.JedisWrapper;
import com.melot.talkee.redis.event.Executor;
import com.melot.talkee.redis.event.RedisEventHandler;
import com.melot.talkee.redis.event.RedisEventReceiver;

/**
 * Title: DBDataEventHandler
 * Description: 数据库数据数据监听
 * </p>
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-04-21 17:30:40 
 */
public class DBDataEventHandler extends Thread implements Executor{

	private static Logger logger = Logger.getLogger(DBDataEventHandler.class);
	private static BlockingQueue<JsonObject> queue = new LinkedBlockingQueue<JsonObject>();
	private final JsonParser parser =  new JsonParser();

	private String subscribePatter;
	
	private JedisWrapper JedisWrapper;
	
	/**
	 * 监听update操作
	 */
	private static final String UPDATE_ACTION = "update";
	
	/**
	 * 监听delete操作
	 */
	private static final String DELETE_ACTION = "delete";

	/**
	 * @param jedisWrapper the jedisWrapper to set
	 */
	public void setJedisWrapper(JedisWrapper jedisWrapper) {
		JedisWrapper = jedisWrapper;
	}

	@Override
	public void execute() {
		afterPropertiesSet();
		this.start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				JsonObject json = queue.take();
				if (json.get("tableName") != null && json.get("action") != null && json.get("time") != null) {
					String tableName = json.get("tableName").getAsString(); //表名
					String action = json.get("action").getAsString();//操作
					String time = json.get("time").getAsString(); //时间
					String keyId = json.get("keyId").getAsString(); //主键ID
					if (StringUtils.isNotBlank(tableName)) {
						
					}
				}
			} catch (InterruptedException e) {
				logger.error(e);
			}catch (Exception e) {
			    logger.error(e);
            }
		}
	}
	
	
	@Override
	public void shutdown() {
		
	}

	public void afterPropertiesSet(){
		RedisEventReceiver redisEventReceiver = new RedisEventReceiver(new RedisEventHandler() {
			@Override
			public void handler(String message) {
				 JsonObject obj = parser.parse(message).getAsJsonObject();
				 try {
					queue.put(obj);
				 } catch (InterruptedException e) {
					 logger.error("handler InterruptedException", e);
				 }catch (Exception e) {
					 logger.error("handler Exception", e);
				}
			}
		}, subscribePatter, JedisWrapper);
		redisEventReceiver.init();
	}

	public void setSubscribePatter(String subscribePatter) {
		this.subscribePatter = subscribePatter;
	}
	
}
