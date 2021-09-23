package com.concordia.seckill.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /*** 设置值 ** @param key * @param value */
    public void setValue(String key, Long value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value.toString());
    }

    /*** 获取值 ** @param key * @return */
    public String getValue(String key) {
        Jedis jedisClient = jedisPool.getResource();
        return jedisClient.get(key);
    }
}
