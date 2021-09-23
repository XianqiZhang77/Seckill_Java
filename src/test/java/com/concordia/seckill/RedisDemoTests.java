package com.concordia.seckill;

import com.concordia.seckill.util.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisDemoTests {

    @Resource
    private RedisService redisService;

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
        redisService.setValue("stock:19", 10L);
    }

    @Test
    public void getStockTest() {
        String stock = redisService.getValue("stock:19");
        System.out.println(stock);
    }
}
