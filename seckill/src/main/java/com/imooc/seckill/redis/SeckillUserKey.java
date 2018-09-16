package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/11.
 */
public class SeckillUserKey extends BasePrefix{
    private static final int EXPIRE_SECOND=3600*24*2;
    public SeckillUserKey(String prefix,int expireSeconds){
        super(expireSeconds,prefix);
    }
    public static SeckillUserKey token=new SeckillUserKey("tk",EXPIRE_SECOND);
}
