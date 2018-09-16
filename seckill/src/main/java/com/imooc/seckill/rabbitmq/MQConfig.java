package com.imooc.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Created by ky on 2018/9/14.
 */
@Configuration
public class MQConfig {
    public static final String QUEUE="queue";
    public static final String TOP_QUEUE1="top.queue1";
    public static final String TOP_QUEUE2="top.queue2";
    public static final String TOPIC_EXCHANG="top.exchange";
    public static final String FANOUT_EXCHANGE="fanout.exchange";
    public static final String HEADER_QUEUE="header.queue";
    public static final String HEADER_EXCHANGE="header.exchange";
    public static final String SECKILL_QUEUE="seckill.queue";
    /*@Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
    //定义两个queue
    @Bean
    public Queue topQueue1(){
        return new Queue(TOP_QUEUE1);
    }
    @Bean
    public Queue topQueue2(){
        return new Queue(TOP_QUEUE2);
    }
    @Bean
    //再定义一个topicExchange
    public TopicExchange topicExchange(){
            return new TopicExchange(TOPIC_EXCHANG);
    }
    @Bean
    public Binding topicBinding1(){
          return BindingBuilder.bind(topQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topicBinding2(){
        //#表示任意多个字符
        return BindingBuilder.bind(topQueue2()).to(topicExchange()).with("topic.#");
    }
    //fanout模式,广播模式
    @Bean
   public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding fanoutBinding1(){
       return BindingBuilder.bind(topQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topQueue2()).to(fanoutExchange());
    }
    //head模式
    @Bean
    public Queue headerQueue(){
        return new Queue(HEADER_QUEUE);
    }
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADER_EXCHANGE);
    }
    @Bean
    public Binding headerBinding(){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("header1","value1");
        map.put("header2","value2");
        //需要消息的头中一定要含有map中的全部键值对,whereAll
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }*/
    //创建秒杀队列
    @Bean
    public Queue seckillQueue(){
        return new Queue(SECKILL_QUEUE);
    }

}
