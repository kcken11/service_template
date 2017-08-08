package com.melot.talkee.redis.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melot.common.melot_jedis.JedisWrapper;
import com.melot.talkee.dao.VersionInfoMapper;
import com.melot.talkee.redis.event.Executor;
import com.melot.talkee.redis.event.RedisEventHandler;
import com.melot.talkee.redis.event.RedisEventReceiver;

/**
 * Title: LastAccessEventHandler
 * Description: 对应版本最后访问时间
 * </p>
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2017-06-27 13:30:40 
 */
public class LastAccessEventHandler extends Thread implements Executor{

	private static Logger logger = Logger.getLogger(LastAccessEventHandler.class);
	private static BlockingQueue<JsonObject> queue = new LinkedBlockingQueue<JsonObject>();
	private final JsonParser parser =  new JsonParser();

	private String subscribePatter;
	
	private JedisWrapper JedisWrapper;
	
	@Autowired
	private VersionInfoMapper versionInfoMapper;

	/**
	 * @param jedisWrapper the jedisWrapper to set
	 */
	public void setJedisWrapper(JedisWrapper jedisWrapper) {
		JedisWrapper = jedisWrapper;
	}

	@Override
	public void execute() {
		afterPropertiesSet();
		this.setName("LastAccessEventHandler");
		this.start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				JsonObject json = queue.take();
				if (json.get("version") != null && json.get("platform") != null) {
					String version = json.get("version").getAsString(); //版本
					Integer platform = json.get("platform").getAsInt();//平台
					if (StringUtils.isNotBlank(version) && platform != null) {
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("versionCode", version);
						param.put("versionPlatform", platform);
					    versionInfoMapper.updateLastAccessTime(param);
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
