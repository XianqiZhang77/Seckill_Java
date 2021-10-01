package com.concordia.seckill.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

@Service
@Slf4j
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /*** 设置值 ** @param key * @param value */
    public void setValue(String key, Long value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value.toString());
    }

    /*** 设置值 ** @param key * @param value */
    public void setValue(String key, String value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value);
    }

    /*** 获取值 ** @param key * @return */
    public String getValue(String key) {
        Jedis jedisClient = jedisPool.getResource();
        return jedisClient.get(key);
    }

    /**
     * 缓存中库存判断和扣减 * @param key * @return * @throws Exception
     */
    public boolean stockDeductValidator(String key) {
        Jedis jedisClient = null;
        try {
            jedisClient = jedisPool.getResource();
            String script =
                    "if redis.call('exists',KEYS[1]) == 1 then\n" +
                            " local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                            " if( stock <=0 ) then\n" +
                            " return -1\n" +
                            " end;\n" +
                            " redis.call('decr',KEYS[1]);\n" +
                            " return stock - 1;\n" +
                            " end;\n" +
                            " return -1;";
            Long stock = (Long) jedisClient
                    .eval(script, Collections.singletonList(key), Collections.emptyList());
            if (stock < 0) {
                System.out.println("库存不足");
                return false;
            } else {
                System.out.println("恭喜，抢购成功");
            }
            return true;
        } catch (Throwable throwable) {
            System.out.println("库存扣减失败：" + throwable.toString());
            return false;
        } finally {
            if (jedisClient != null) {
                jedisClient.close();
            }
        }
    }

    /**
     * 超时未支付 Redis 库存回滚
     *
     * @param key
     */
    public void revertStock(String key) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.incr(key);
        jedisClient.close();
    }

    public boolean isInLimitMember(long seckillActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        boolean sismember = jedisClient
                .sismember("seckillActivity_users:" + seckillActivityId, String.valueOf(userId));
        log.info("userId:{} activityId:{} 在已购名单中:{}", userId, seckillActivityId, sismember);
        return sismember;
    }

    public void addLimitMember(long seckillActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd("seckillActivity_users:" + seckillActivityId, String.valueOf(userId));
    }

    /**
     * 移除限购名单
     *
     * @param seckillActivityId
     * @param userId
     */
    public void removeLimitMember(Long seckillActivityId, Long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("seckillActivity_users:" + seckillActivityId, String.valueOf(userId));
    }

    /**
     * 获取分布式锁
     *
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedisClient = jedisPool.getResource();
        //expx 参数有两个值可选 ：
        // EX： seconds 秒
        // PX: milliseconds 毫秒
        String result = jedisClient.set(lockKey, requestId, "NX", "PX", expireTime);
        jedisClient.close();
        return "OK".equals(result);
    }

    /**
     *  释放分布式锁
     *  @param lockKey 锁
     *  @param requestId 请求标识
     *  @return 是否释放成功
     *  */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedisClient = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = (Long) jedisClient.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        jedisClient.close();
        return result == 1L;
    }
}
