package com.concordia.seckill.db.dao;

import com.concordia.seckill.db.po.SeckillActivity;
import java.util.List;

public interface SeckillActivityDao {

    public List<SeckillActivity> querySeckillActivitysByStatus(int activityStatus);

    public void insertSeckillActivity(SeckillActivity seckillActivity);

    public SeckillActivity querySeckillActivityById(long activityId);

    public void updateSeckillActivity(SeckillActivity seckillActivity);

    boolean lockStock(Long seckillActivityId);
}
