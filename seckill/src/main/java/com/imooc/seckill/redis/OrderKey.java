package com.imooc.seckill.redis;

/**
 * Created by ky on 2018/9/15.
 */
public class OrderKey extends BasePrefix{
    public static final OrderKey getSeckillOrderByUserIdAndGoodsId=new OrderKey("moug");
public OrderKey(String prefix){
    super(prefix);
}
}
