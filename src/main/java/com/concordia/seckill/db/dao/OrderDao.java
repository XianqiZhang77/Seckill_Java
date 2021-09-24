package com.concordia.seckill.db.dao;

import com.concordia.seckill.db.po.Order;

public interface OrderDao {
    void insertOrder(Order order);

    Order queryOrder(String orderNo);

    void updateOrder(Order order);
}
