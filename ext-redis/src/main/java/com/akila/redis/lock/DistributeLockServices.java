package com.akila.redis.lock;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class DistributeLockServices {

    private final RedissonPool redissonPool;

    public DistributeLockServices(RedissonPool redissonPool) {
        this.redissonPool = redissonPool;
    }

    public boolean lock (String key, long expireSeconds) {
        try {
            var lock = this.redissonPool.getRedissonClient().getLock(key);
            lock.lock(expireSeconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    public boolean tryLock (String key, long timeOutSeconds, long expireSeconds) {
        try {
            var lock = this.redissonPool.getRedissonClient().getLock(key);
            return lock.tryLock(timeOutSeconds, expireSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    public void unlock (String key) {
        try {
            var lock = this.redissonPool.getRedissonClient().getLock(key);
            lock.unlock();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
