package com.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Collection;
import java.util.Set;

public class SpringCache implements Cache {

    private org.springframework.cache.Cache springCache;

    SpringCache(org.springframework.cache.Cache springCache) {
        this.springCache = springCache;
    }

    public Object get(Object key) throws CacheException {
        Object value = springCache.get(key);
        if (value instanceof SimpleValueWrapper) {
            return ((SimpleValueWrapper) value).get();
        }
        return value;
    }

    public Object put(Object key, Object value) throws CacheException {
        springCache.put(key, value);
        return value;
    }

    public Object remove(Object key) throws CacheException {
        springCache.evict(key);
        return null;
    }

    public void clear() throws CacheException {
        springCache.clear();
    }

    public int size() {
        throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");
    }

    public Set keys() {
        throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
    }


    public Collection values() {
        throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
    }
}
