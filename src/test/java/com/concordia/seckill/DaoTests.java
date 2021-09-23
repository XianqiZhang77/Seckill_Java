package com.concordia.seckill;

import com.concordia.seckill.db.dao.SeckillActivityDao;
import com.concordia.seckill.db.mappers.SeckillActivityMapper;
import com.concordia.seckill.db.mappers.SeckillCommodityMapper;
import com.concordia.seckill.db.po.SeckillActivity;
import com.concordia.seckill.db.po.SeckillCommodity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class DaoTests {

    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Resource
    private SeckillCommodityMapper seckillCommodityMapper;

    @Autowired
    private SeckillActivityDao seckillActivityDao;


    @Test
    void SeckillActivityTest() {
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName("测试");
        seckillActivity.setCommodityId(999L);
        seckillActivity.setTotalStock(100L);
        seckillActivity.setSeckillPrice(new BigDecimal(99));
        seckillActivity.setActivityStatus(16);
        seckillActivity.setOldPrice(new BigDecimal(199));
        seckillActivity.setAvailableStock(111);
        seckillActivity.setLockStock(0L);
        seckillActivityMapper.insert(seckillActivity);
        System.out.println("====>>>>" + seckillActivityMapper.selectByPrimaryKey(1L));
    }


    @Test
    void SeckillCommodityTest() {
        SeckillCommodity seckillCommodity = new SeckillCommodity();
        seckillCommodity.setCommodityName("11");
        seckillCommodity.setCommodityDesc("11");
        seckillCommodity.setCommodityPrice(100);
        seckillCommodityMapper.insert(seckillCommodity);
        seckillCommodityMapper.selectByPrimaryKey(11L);
        System.out.println("====>>>>" + seckillCommodityMapper.selectByPrimaryKey(11L));
    }

    @Test
    void setSeckillActivityQuery(){
        List<SeckillActivity> seckillActivitys = seckillActivityDao.querySeckillActivitysByStatus(0);
        System.out.println(seckillActivitys.size());
        seckillActivitys.stream().forEach(seckillActivity -> System.out.println(seckillActivity.toString()));
    }
}
