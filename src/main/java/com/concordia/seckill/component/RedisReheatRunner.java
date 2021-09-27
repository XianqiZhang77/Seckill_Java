package com.concordia.seckill.component;

import com.concordia.seckill.db.dao.SeckillActivityDao;
import com.concordia.seckill.db.po.SeckillActivity;
import com.concordia.seckill.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisReheatRunner implements ApplicationRunner {
    @Autowired
    RedisService redisService;
    @Autowired
    SeckillActivityDao seckillActivityDao;

    /**
     * 商品缓存预热
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SeckillActivity> seckillActivities = seckillActivityDao.querySeckillActivitysByStatus(1);
        for (SeckillActivity seckillActivity : seckillActivities) {
            redisService.setValue("stock:" + seckillActivity.getId(), (long) seckillActivity.getAvailableStock());
        }
    }
}