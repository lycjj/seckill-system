package com.imooc.seckill.rabbitmq;

import com.imooc.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ky on 2018/9/14.
 */
@Service
public class MQSender {
    @Autowired
    AmqpTemplate amqpTemplate;
    //打印一下
    private Logger logger= LoggerFactory.getLogger(MQReceive.class);
    /*public void send(Object message){
     String msg=RedisService.beanToString(message);
logger.info("send message:"+msg);
     amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }*/
    /*public void sendTopic(Object message){
                 String msg=RedisService.beanToString(message);
                 logger.info("send message"+msg);
                 amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANG,"topic.key1",msg+"1");
                 amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANG,"topic.key2",msg+"2");
    }
    public void sendFanout(Object message){
        String msg=RedisService.beanToString(message);
        logger.info("send message"+msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
    }
    public void sendHeader(Object message){
        String msg=RedisService.beanToString(message);
        logger.info("header send message:"+msg);
        MessageProperties pro=new MessageProperties();
        pro.setHeader("header1","value1");
        pro.setHeader("header2","value2");
        Message mess=new Message(msg.getBytes(),pro);
          amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE,"",mess);
    }
*/
    public void sendSeckillMessage(SeckillMessage message){
        String msg=RedisService.beanToString(message);
        logger.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
    }
}
