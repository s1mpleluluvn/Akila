/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author s1mpl
 */
@Component
public class RedisQueueService {

    @Autowired
    private RedisPoolImpl pool;

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

    //LREM
    public Long removeQueue(String key, String value) {
        try (var jedis = this.pool.getSession()) {
            return jedis.lrem(key, 0, value);
        }
    }
}
