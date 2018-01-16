package com.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RedisCache<K, V> implements Cache<K, V> {

    private RedisTemplate<K, V> redisTemplate;

    private String keyPrefix;

    @SuppressWarnings("rawtypes")
    public RedisCache(String name, RedisTemplate<K, V> redisTemplate) {
        this.keyPrefix = name;
        this.redisTemplate = redisTemplate;
    }

    public V get(K key) throws CacheException {
        return redisTemplate.opsForValue().get(getKey(key));
    }

    public V put(K key, V value) throws CacheException {
        V old = get(key);
        redisTemplate.opsForValue().set(getKey(key),value);
        return old;
    }

    public V remove(K key) throws CacheException {
        V old = get(key);
        redisTemplate.delete(getKey(key));
        return old;
    }

    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    public int size() {
        return keys().size();
    }

    public Set<K> keys() {
        return redisTemplate.keys(getKey("*"));
    }

    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<V>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    public K getKey(Object key){
        return  (K) (keyPrefix + key);
    }
}
