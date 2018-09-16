package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/15.
 */
public class SeckillKey extends BasePrefix {
    public SeckillKey( int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }
    public static final SeckillKey GOODS_OVER=new SeckillKey(0,"go");
    public static final SeckillKey GET_SECKILL_PATH=new SeckillKey(60,"sp");
    public static final SeckillKey GET_SECKILL_VERIFY_CODE=new SeckillKey(300,"vc");
}
