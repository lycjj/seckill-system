package com.imooc.seckill.Vo;

import com.imooc.seckill.domain.SeckillGoods;
import com.imooc.seckill.domain.SeckillUser;

/**
 * Created by ky on 2018/9/13.
 */
public class GoodsDetailVo {
    private SeckillUser user;
    private GoodsVo goods;
    private int remainSecond;
    private int status;

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    public int getRemainSecond() {
        return remainSecond;
    }

    public void setRemainSecond(int remainSecond) {
        this.remainSecond = remainSecond;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
}
