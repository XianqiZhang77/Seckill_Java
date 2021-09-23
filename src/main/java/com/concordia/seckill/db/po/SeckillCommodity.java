package com.concordia.seckill.db.po;

public class SeckillCommodity {
    private Long id;

    private String commodityName;

    private String commodityDesc;

    private Integer commodityPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc == null ? null : commodityDesc.trim();
    }

    public Integer getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(Integer commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public SeckillCommodity(Long id, String commodityName, String commodityDesc, Integer commodityPrice) {
        this.id = id;
        this.commodityName = commodityName;
        this.commodityDesc = commodityDesc;
        this.commodityPrice = commodityPrice;
    }

    public SeckillCommodity() {
    }

    @Override
    public String toString() {
        return "SeckillCommodity{" +
                "id=" + id +
                ", commodityName='" + commodityName + '\'' +
                ", commodityDesc='" + commodityDesc + '\'' +
                ", commodityPrice=" + commodityPrice +
                '}';
    }
}