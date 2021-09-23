package com.concordia.seckill.db.po;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class SeckillActivity {
    private Long id;

    private String name;

    private Long commodityId;

    private BigDecimal oldPrice;

    private BigDecimal seckillPrice;

    private Integer activityStatus;

    private Date startTime;

    private Date endTime;

    private Long totalStock;

    private Integer availableStock;

    private Long lockStock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BigDecimal getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Long totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Long getLockStock() {
        return lockStock;
    }

    public void setLockStock(Long lockStock) {
        this.lockStock = lockStock;
    }

    public SeckillActivity(Long id, String name, Long commodityId, BigDecimal oldPrice, BigDecimal seckillPrice,
                           Integer activityStatus, Date startTime, Date endTime,
                           Long totalStock, Integer availableStock, Long lockStock) {
        this.id = id;
        this.name = name;
        this.commodityId = commodityId;
        this.oldPrice = oldPrice;
        this.seckillPrice = seckillPrice;
        this.activityStatus = activityStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalStock = totalStock;
        this.availableStock = availableStock;
        this.lockStock = lockStock;
    }

    public SeckillActivity() {

    }

    @Override
    public String toString() {
        return "SeckillActivity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", commodityId=" + commodityId +
                ", oldPrice=" + oldPrice +
                ", seckillPrice=" + seckillPrice +
                ", activityStatus=" + activityStatus +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalStock=" + totalStock +
                ", availableStock=" + availableStock +
                ", lockStock=" + lockStock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeckillActivity that = (SeckillActivity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(commodityId, that.commodityId) && Objects.equals(oldPrice, that.oldPrice) && Objects.equals(seckillPrice, that.seckillPrice) && Objects.equals(activityStatus, that.activityStatus) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(totalStock, that.totalStock) && Objects.equals(availableStock, that.availableStock) && Objects.equals(lockStock, that.lockStock);
    }

}