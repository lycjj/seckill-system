package com.imooc.seckill.rabbitmq;

import com.imooc.seckill.domain.SeckillUser;

/**
 * Created by ky on 2018/9/15.
 */
public class SeckillMessage {
    private SeckillUser user;
    private Long goodsId;

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
