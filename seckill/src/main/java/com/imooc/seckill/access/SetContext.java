package com.imooc.seckill.access;

import com.imooc.seckill.domain.SeckillUser;

/**
 * Created by ky on 2018/9/16.
 */
public class SetContext {
    private static ThreadLocal<SeckillUser> local=new ThreadLocal<SeckillUser>();
    public static void setUser(SeckillUser user){
        local.set(user);
    }
    public static SeckillUser getUser(){
        return local.get();
    }
}
