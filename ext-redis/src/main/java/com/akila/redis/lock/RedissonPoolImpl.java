package com.akila.redis.lock;

import org.springframework.beans.factory.annotation.Value;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

@Component
public class RedissonPoolImpl implements RedissonPool{

    private RedissonClient redissonClient;

    public RedissonPoolImpl(
        @Value("${redis.host}") String redisHost,
        @Value("${redis.port}") int redisPort,
        @Value("${redis.password}") String redisPassword) {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://"+redisHost+":"+redisPort)
            .setPassword(redisPassword) // Set the Redis password here
            .setConnectionPoolSize(50); // Set the connection pool size
        this.redissonClient = Redisson.create(config);
    }

    @Override
    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }
}
