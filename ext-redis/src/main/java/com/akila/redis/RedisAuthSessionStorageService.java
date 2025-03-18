/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.akila.redis.RedisPoolImpl;
import com.akila.util.JsonUtil;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author minh
 */
@Component
public class RedisAuthSessionStorageService {

    private static final String USERAUTH_NAMESPACE = "USER_AUTH";

    @Autowired
    private RedisPoolImpl pool;

    private final JsonMapper jsonMapper = JsonUtil.getJsonMapper();

    public void addQueue(String key, String value) {
        try (var jedis = this.pool.getSession()) {
            jedis.lpush(key, value);
        }
    }

    public String popQueue(String key) {
        try (var jedis = this.pool.getSession()) {
            return jedis.rpop(key);
        }
    }

    public Long getQueueSize(String key) {
        try (var jedis = this.pool.getSession()) {
            return jedis.llen(key);
        }
    }

    private static String getRedisKey(String authKey) {
        // form key as <NAMESPACE>:<auth_key>
        return String.format("%s:%s", USERAUTH_NAMESPACE, authKey);
    }

    public void putSession(String key, AuthenticationSession session, OffsetDateTime expiration) {
        try (var jedis = this.pool.getSession()) {
            var value = this.jsonMapper.writeValueAsString(session);
            var redisKey = getRedisKey(key);

            var secondsToExpiration = Duration.between(OffsetDateTime.now(), expiration).toSeconds();
            if (secondsToExpiration < 0) {
                throw new IllegalArgumentException("expiration must be in the future");
            }

            //use transaction to ensure key has expiration
            var tx = jedis.multi();
            tx.set(redisKey, value);
            tx.expire(redisKey, secondsToExpiration);
            tx.exec();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialzie session to JSON", e);
        }
    }

    public AuthenticationSession getSession(String key) {
        try (var jedis = this.pool.getSession()) {
            var redisKey = getRedisKey(key);
            var value = jedis.get(redisKey);
            if (value == null) {
                return null;
            }

            return this.jsonMapper.readValue(value, AuthenticationSession.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot deserialize session", e);
        }
    }

    public Map<String, AuthenticationSession> getSessions(String keyPrefix) {
        try (var jedis = this.pool.getSession()) {

            var allRedisKeys = jedis.keys(String.format("%s%s", getRedisKey(keyPrefix), "*"));

            var sessions = new HashMap<String, AuthenticationSession>();
            for (String redisKey : allRedisKeys) {

                var key = getKeyFormRedisKey(redisKey);
                var session = this.getSession(key);

                sessions.put(key, session);
            }
            return sessions;
        }
    }

    public boolean deleteSession(String key) {
        try (var jedis = this.pool.getSession()) {
            var redisKey = getRedisKey(key);
            return jedis.del(redisKey) > 0;
        }
    }

    public void deleteAllSessionsMatch(String prefix) {
        var sessions = this.getSessions(prefix);
        for (var key : sessions.keySet()) {
            this.deleteSession(key);
        }
    }

    private static String getKeyFormRedisKey(String redisKey) {
        if (redisKey == null) {
            return null;
        }

        if (!redisKey.matches(String.format("%s:%s", USERAUTH_NAMESPACE, "(.*)"))) {
            return null;
        }

        return redisKey.substring(USERAUTH_NAMESPACE.length() + 1);
    }
}
