package com.concordia.seckill;

import com.concordia.seckill.services.SeckillActivityService;
import com.concordia.seckill.util.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisDemoTests {

    @Resource
    private RedisService redisService;

    @Resource
    private SeckillActivityService seckillActivityService;

    @Test
    public void setTest() {
        redisService.setValue("age", 100L);
    }

    @Test
    public void getTest() {
        String age = redisService.getValue("age");
        System.out.println(age);
    }


    @Test
    public void stockTest() {
        redisService.setValue("stock:12", 10L);
    }

    @Test
    public void getStockTest() {
        String stock = redisService.getValue("stock:12");
        System.out.println(stock);
    }

    @Test
    public void stockDeductValidatorTest() {
        boolean result = redisService.stockDeductValidator("stock:12");
        System.out.println("result:" + result);
        String stock = redisService.getValue("stock:12");
        System.out.println("stock:" + stock);
    }

    @Test
    public void pushSeckillInfoToRedisTest() {
        seckillActivityService.pushSeckillInfoToRedis(19);
    }

    @Test
    public void getSeckillInfoFromRedis() {
        String seckillActivityInfo = redisService.getValue("seckillActivity:" + 19);
        System.out.println(seckillActivityInfo);

        String seckillCommodityInfo = redisService.getValue("seckillCommodity:" + 1001);
        System.out.println(seckillCommodityInfo );
    }
}
