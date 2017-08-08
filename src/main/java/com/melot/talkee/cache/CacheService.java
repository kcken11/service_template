package com.melot.talkee.cache;

import java.util.List;
import java.util.Map;

/**
 * 本地缓存服务
 * Title: CacheService
 * <p>
 * Description: 减少不常用配置项 频繁查询DB和redis
 * </p>
 * 
 * @author 董毅<a href="mailto:yi.dong@melot.cn" />
 * @version V1.0
 * @since 2017-6-22 下午6:37:04
 */
public interface CacheService {

    /**
     * 初始化缓存
     * 
     */
    public void init();
    /**
     * 根据关键字从缓存里获取一个对象
     * 
     * @param key
     *            该对象的缓存关键字
     * @return 被缓存的对象
     */
    public Object getFromCache(String key);
    
    /**
     * 根据关键字从缓存里获取一个对象
     * @param <T> 
     * @param key 该对象的缓存关键字
     * @param clazz 对象类型
     * @return
     */
    public <T> T getFromCache(String key, Class<T> clazz);
    
    /**
     * 批量获取缓存里的对象
     * @param keys key列表
     * @return
     */
    public Map<String, Object> getBulk(List<String> keys);

    /**
     * 存放一个对象到缓存里
     * 
     * @param key
     *            该对象的缓存关键字
     * @param value
     *            被缓存的对象
     * @param expirySeconds
     *            失效时间，如10，表示10秒后失效
     * 
     */
    public void putInCache(String key, Object value, int expirySeconds);
    
    
    /**
     * 存放一个对象到缓存里
     * 
     * @param key
     *            该对象的缓存关键字
     * @param value
     *            被缓存的对象
     */
    public void putInCache(String key, Object value);

    /**
     * 从缓存里删除一个对象
     * 
     * @param key
     *            该对象的缓存关键字
     */
    public void removeFromCache(String key);

    /**
     * 取缓存对象的数量
     * 
     * @return 缓存对象的数量
     */
    public int size();

    /**
     * 清除缓存里所有东西，缓存初始化
     * 
     */
    public void reset();

    /**
     * 破坏缓存
     * 
     */
    public void destroy();

}
