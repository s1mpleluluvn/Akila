package com.akila.redis.lock;

import org.redisson.api.RedissonClient;

public interface RedissonPool {

    RedissonClient getRedissonClient();

}
