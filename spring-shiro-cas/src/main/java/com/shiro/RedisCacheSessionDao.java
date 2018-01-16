package com.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class RedisCacheSessionDao extends CachingSessionDAO {

    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        return sessionId;
    }

    //父类已经实现
    protected Session doReadSession(Serializable sessionId) {
        return null;
    }

    //父类已经实现
    protected void doUpdate(Session session) {


    }

    //父类已经实现
    protected void doDelete(Session session) {


    }



}
