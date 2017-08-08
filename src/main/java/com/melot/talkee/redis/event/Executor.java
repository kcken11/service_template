package com.melot.talkee.redis.event;

/**
 * 所有初始化启动的线程必须继承本接口
 * 
 */
public interface Executor {

	/**
	 * executor 具体调用的方法
	 * 
	 */
	void execute();

	void shutdown();

}
