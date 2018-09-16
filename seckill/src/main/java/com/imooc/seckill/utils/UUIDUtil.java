package com.imooc.seckill.utils;

import java.util.UUID;

/**
 * Created by ky on 2018/9/11.
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
