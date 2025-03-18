package com.akila.redis;

import redis.clients.jedis.Jedis;

/**
 * Manage application-wide Redis connection pool.
 */
public interface RedisPool {
    Jedis getSession();
}
