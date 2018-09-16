package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/12.
 */
public class GoodsKey extends BasePrefix{
    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static final GoodsKey getGoodsList=new GoodsKey(60*60,"gl");
    public static final GoodsKey getGoodsDetail=new GoodsKey(60*30,"dl");
    public static final GoodsKey getMiaoshaGoodsStock=new GoodsKey(0,"gs");
}
