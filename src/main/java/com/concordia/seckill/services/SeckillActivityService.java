package com.concordia.seckill.services;

import com.alibaba.fastjson.JSON;
import com.concordia.seckill.db.dao.OrderDao;
import com.concordia.seckill.db.dao.SeckillActivityDao;
import com.concordia.seckill.db.po.Order;
import com.concordia.seckill.db.po.SeckillActivity;
import com.concordia.seckill.mq.RocketMQService;
import com.concordia.seckill.util.RedisService;
import com.concordia.seckill.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SeckillActivityService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SeckillActivityDao seckillActivityDao;
    @Autowired
    private RocketMQService rocketMQService;
    @Autowired
    private OrderDao orderDao;

    /**
     * 判断商品是否还有库存 * @param activityId 商品ID * @return
     */
    public boolean seckillStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }

    /***
     * datacenterId; 数据中心
     * machineId; 机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死 */
    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    /**
     * 创建订单
     *
     * @param seckillActivityId
     * @param userId
     * @return
     * @throws Exception
     */
    public Order createOrder(long seckillActivityId, long userId) throws Exception {
        /* 1.创建订单 */
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();
        //采用雪花算法生成订单ID
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(seckillActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(seckillActivity.getSeckillPrice().longValue());
        /* 2.发送创建订单消息 */
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));
        return order;
    }


    public void payOrderProcess(String orderNo) {
        log.info("完成支付订单 订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        boolean deductStockResult = seckillActivityDao
                .deductStock(order.getSeckillActivityId());
        if (deductStockResult) {
            order.setPayTime(new Date());
            // 订单状态 0、没有可用库存，无效订单 1、已创建等待支付 2、完成支付
            order.setOrderStatus(2);
            orderDao.updateOrder(order);
        }
    }
}
