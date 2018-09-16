package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/10.
 */
public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
