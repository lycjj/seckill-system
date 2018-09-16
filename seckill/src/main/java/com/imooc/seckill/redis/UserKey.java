package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/10.
 */
public class UserKey extends BasePrefix{
    private UserKey(String prefix) {
        super(prefix);
    }
    public static UserKey getById=new UserKey("id");
    public static UserKey getByName=new UserKey("name");
}
