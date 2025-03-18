package com.akila.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component("ext-redis")
public class RedisPoolImpl implements RedisPool {
    private final JedisPool jedisPool;

    public RedisPoolImpl(
        @Value("${redis.host}") String redisHost,
        @Value("${redis.port}") int redisPort,
        @Value("${redis.password}") String redisPassword) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(100);
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxWaitMillis(30000);
        poolConfig.setMinIdle(10);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setNumTestsPerEvictionRun(8);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(300000);

        this.jedisPool = new JedisPool(poolConfig, redisHost, redisPort, 10000, redisPassword, false);
    }

    @Override
    public Jedis getSession() {
        return this.jedisPool.getResource();
    }
}
