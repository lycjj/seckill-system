package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/15.
 */
public class AccessKey extends BasePrefix{
    public AccessKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }
    public static final AccessKey ACCESS_SECKILL=new AccessKey(5,"access");
    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }
}
