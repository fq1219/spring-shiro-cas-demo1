package com.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
* 自定义实现redis缓存session，相当于RedisCacheSessionDao
* */
public class RedisCachingSessionDao extends AbstractSessionDAO {

    private RedisTemplate<String, Session> redisTemplate;

    // session 在redis过期时间是30分钟30*60
    private int expireTime = 1800;

    private static String prefix = "shiro-session:";

    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        redisTemplate.opsForValue().set(prefix + sessionId.toString(), session, expireTime, TimeUnit.SECONDS);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        Session session = redisTemplate.opsForValue().get(prefix + sessionId.toString());
        return session;
    }

    public void update(Session session) throws UnknownSessionException {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return; //如果会话过期/停止 没必要再更新了
        }
        Serializable sessionId = session.getId();
        String key = prefix + sessionId.toString();
        if(!redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().set(prefix + sessionId.toString(), session);
        }
        redisTemplate.expire(prefix + sessionId.toString(), expireTime, TimeUnit.SECONDS);

    }

    public void delete(Session session) {
        Serializable sessionId = session.getId();
        redisTemplate.delete(prefix + sessionId.toString());
    }


    public Collection<Session> getActiveSessions() {
        Set<String> set = redisTemplate.keys(prefix+"*");
        List<Session> list = new ArrayList<Session>();
        for (String s : set) {
            list.add(redisTemplate.opsForValue().get(s));
        }
        return list;
    }



    public RedisTemplate<String, Session> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
