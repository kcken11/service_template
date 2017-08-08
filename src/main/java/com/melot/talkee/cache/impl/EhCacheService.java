package com.melot.talkee.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;

import com.melot.talkee.cache.CacheConfig;
import com.melot.talkee.cache.CacheService;

@Service
public class EhCacheService implements CacheService{
    
    private CacheConfig cacheConfig;
    
    private CacheManager cacheManager;
    
    private Cache enCache;
    
    /**
     * @param cacheConfig the cacheConfig to set
     */
    public void setCacheConfig(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void destroy() {
        if (enCache != null) {
            if (cacheManager != null) {
                cacheManager.removeCache(cacheConfig.getName());
            }
            enCache.removeAll();
            enCache = null;
        }
    }
    
    @Override
    public Map<String, Object> getBulk(List<String> keys) {
        Map<Object, Element> values = enCache.getAll(keys);
        Map<String, Object> vals = null;
        if (values != null && values.size() > 0) {
            vals = new HashMap<String, Object>(values.size());
            for (Entry<Object, Element> entry : values.entrySet()) {
                vals.put((String)entry.getKey(), ((Element)entry.getValue()).getObjectKey());
            }
            values.clear();
        }
        return vals;
    }

    @Override
    public Object getFromCache(String key) {
        Element cacheEl = enCache.get(key);
        if (cacheEl != null) {
            return cacheEl.getObjectValue();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getFromCache(String key, Class<T> clazz) {
        Element cacheEl = enCache.get(key);
        if (cacheEl != null) {
            return (T)cacheEl.getObjectValue();
        } 
        return null;
    }
    
    @Override
    public void init() {
        Configuration configuration = new Configuration();
        configuration.setDynamicConfig(true);
        configuration.addCache(cacheConfig);
        configuration.setName(cacheConfig.getName());
        cacheManager = CacheManager.create(configuration);
        
        enCache = cacheManager.getCache(cacheConfig.getName());
        if (enCache == null) {
            enCache = new Cache(cacheConfig);
            enCache.setName(cacheConfig.getName());
            cacheManager.addCache(enCache);
        }
    }
    
    @Override
    public void putInCache(String key, Object value, int expirySeconds) {
        Element el = new Element(key, value, 0, expirySeconds);
        enCache.put(el);
    }
    
    @Override
    public void putInCache(String key, Object value) {
        Element el = new Element(key, value);
        enCache.put(el);
    }
    
    @Override
    public void removeFromCache(String key) {
        enCache.remove(key);
    }
    
    @Override
    public void reset() {
        enCache.removeAll();
    }

    @Override
    public int size() {
        return enCache.getSize();
    }
    
}
