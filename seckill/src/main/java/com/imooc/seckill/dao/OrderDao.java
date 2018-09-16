package com.imooc.seckill.dao;

import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;

/**
 * Created by ky on 2018/9/12.
 */
@Mapper
public interface OrderDao {
    @Select("select * from seckill_order where user_id=#{userId} and goods_id=#{goodsId}")
    public SeckillOrder getSeckillOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);
@Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values(#{userId},#{goodsId}," +
        "#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
@SelectKey(keyColumn = "id",keyProperty = "id",resultType = Long.class,before = false,statement = "select last_insert_id()")
    public Long insertOrder(OrderInfo orderInfo);
@Insert("insert into seckill_order(user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId})")
public int insertSeckillOrder(SeckillOrder order);
@Select("select * from order_info where id=#{orderId}")
public OrderInfo getOrderById(@Param(value = "orderId") Long orderId);
}
