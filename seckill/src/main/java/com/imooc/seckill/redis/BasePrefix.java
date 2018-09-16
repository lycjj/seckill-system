package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/10.
 */
public abstract class BasePrefix implements KeyPrefix{
    private int expireSeconds;
    private String prefix;

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
public BasePrefix(String prefix){
        this(0,prefix);
}
    //过期时间,若为0则代表永不过期
    public int expireSeconds(){
        return this.expireSeconds;
    }
    public String getPrefix(){
        String className=getClass().getSimpleName();
        return className+":"+this.prefix;
    }
}
