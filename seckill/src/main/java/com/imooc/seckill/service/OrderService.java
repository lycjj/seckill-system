package com.imooc.seckill.service;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.dao.OrderDao;
import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.domain.SeckillOrder;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.redis.GoodsKey;
import com.imooc.seckill.redis.OrderKey;
import com.imooc.seckill.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * Created by ky on 2018/9/12.
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;
public SeckillOrder getSeckillOrderByUserIdAndGoodsId(long userId,long goodsId){
    //利用缓存改进
   return redisService.get(OrderKey.getSeckillOrderByUserIdAndGoodsId,userId+"_"+goodsId,SeckillOrder.class);
}
@Transactional
    public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
//创建订单
        OrderInfo orderInfo=new OrderInfo();
        Date date=new Date();
        orderInfo.setCreateDate(date);
        orderInfo.setDeliveryAddrId(0L);
        //下单的商品数
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setStatus(0);
        orderInfo.setOrderChannel(1);
        orderInfo.setUserId(user.getId());
        //插入之后,mybatis会将得到的orderId放入orderInfo对象中
       orderDao.insertOrder(orderInfo);
       SeckillOrder seckillOrder=new SeckillOrder();
       seckillOrder.setGoodsId(goods.getId());
       seckillOrder.setOrderId(orderInfo.getId());
       seckillOrder.setUserId(user.getId());
        orderDao.insertSeckillOrder(seckillOrder);
        //在向数据库中插入秒杀订单时也将数据存入redis
        redisService.set(OrderKey.getSeckillOrderByUserIdAndGoodsId,user.getId()+"_"+goods.getId(),seckillOrder);
        return orderInfo;
    }

    public OrderInfo getOrderById(Long orderId) {
    return orderDao.getOrderById(orderId);
    }
}
