package com.imooc.seckill.Vo;

import com.imooc.seckill.domain.Goods;

import java.util.Date;

/**
 * Created by ky on 2018/9/11.
 */
public class GoodsVo extends Goods{
    /*seckill_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '秒杀的商品价格',
stock_count INT(11) DEFAULT NULL COMMENT '库存数量',
start_date DATETIME DEFAULT NULL COMMENT '秒杀开始时间',
end_date*/
    private Double seckillPrice;
    private int stockCount;
    private Date startDate;
    private Date endDate;

    public Double getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Double seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
