package com.concordia.seckill.services;

import com.alibaba.fastjson.JSON;
import com.concordia.seckill.db.dao.OrderDao;
import com.concordia.seckill.db.dao.SeckillActivityDao;
import com.concordia.seckill.db.dao.SeckillCommodityDao;
import com.concordia.seckill.db.po.Order;
import com.concordia.seckill.db.po.SeckillActivity;
import com.concordia.seckill.db.po.SeckillCommodity;
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
    @Autowired
    private SeckillCommodityDao seckillCommodityDao;

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
        /*
         * 3.发送订单付款状态校验消息
         * 开源RocketMQ支持延迟消息，但是不支持秒级精度。默认支持18个level的延迟消息
         * 这是通 过broker端的messageDelayLevel配置项确定的, 如下：
         * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
         */
        rocketMQService.sendDelayMessage("pay_check", JSON.toJSONString(order), 5);
        return order;
    }


    public void payOrderProcess(String orderNo) throws Exception {

        log.info("正在查询支付订单 订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        /*
         * 1. 判断订单是否存在
         * 2. 判断订单状态是否为未支付状态
         */
        if (order == null) {
            log.error("对应订单不存在" + orderNo);
            return;
        }
        if (order.getOrderStatus() != 1) {
            log.error("订单状态无效" + orderNo);
            return;
        }
        /*
         *  订单支付完成
         */
        order.setPayTime(new Date());
        // 订单状态 0、没有可用库存，无效订单 1、已创建等待支付 2、完成支付
        order.setOrderStatus(2);
        orderDao.updateOrder(order);

        /*
         *发送订单付款成功消息
         */
        rocketMQService.sendMessage("pay_done", JSON.toJSONString(order));
    }

    /*** 将秒杀详情相关信息倒入redis * @param seckillActivityId */
    public void pushSeckillInfoToRedis(long seckillActivityId) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        redisService.setValue("seckillActivity:" + seckillActivityId, JSON.toJSONString(seckillActivity));
        SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(seckillActivity.getCommodityId());
        redisService.setValue("seckillCommodity:" + seckillActivity.getCommodityId(), JSON.toJSONString(seckillCommodity));
    }
}
