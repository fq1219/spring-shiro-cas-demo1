package com.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;

public class RedisCacheManager implements CacheManager {

    private RedisTemplate<String, Object> redisTemplate;

    private ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

    /**
     * The Redis key prefix for caches
     */
    private String keyPrefix = "shiro_redis_cache:";

    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        String key = keyPrefix + name + ":";
        Cache cache;
        if(cacheMap.containsKey(name)){
            cache = cacheMap.get(name);
        }else{
            cache= new RedisCache(key, redisTemplate);
            cacheMap.put(name, cache);
        }
        return cache;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
