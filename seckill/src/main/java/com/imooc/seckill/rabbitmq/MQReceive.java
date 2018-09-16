package com.imooc.seckill.rabbitmq;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.domain.SeckillOrder;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ky on 2018/9/14.
 */
@Service
public class MQReceive {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    SeckillService seckillService;
    //打印一下
    private Logger logger= LoggerFactory.getLogger(MQReceive.class);
    /*@RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){
          logger.info("receive message:"+message);
    }*/
/*@RabbitListener(queues = MQConfig.TOP_QUEUE1)
    public void receiveTopic1(String message){
             logger.info("topic queue1 receive message:"+message);
    }
    @RabbitListener(queues = MQConfig.TOP_QUEUE2)
    public void receiveTopic2(String message){
        logger.info("topic queue2 receive message:"+message);
    }
    //header接收
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeader(byte[] message){
        logger.info("header queue receive:"+new String(message));
    }*/
    @RabbitListener(queues=MQConfig.SECKILL_QUEUE)
    public void receiveSeckill(String message){
        //打印message
        logger.info("SeckillQueue receive:"+message);
        //将message转成对象
            SeckillMessage seckillMessage= RedisService.stringToBean(message,SeckillMessage.class);
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(seckillMessage.getGoodsId());
        long stock=goods.getStockCount();
        //若库存不足
        if(stock<=0){
            return;

        }
        //若重复秒杀
        SeckillOrder order=orderService.getSeckillOrderByUserIdAndGoodsId(seckillMessage.getUser().getId(),seckillMessage.getGoodsId());
        if(order!=null){
            return;
        }
        //若可以秒杀,则需要执行减库存、下订单、写入秒杀订单
        seckillService.seckill(seckillMessage.getUser(),goods);
    }
}
