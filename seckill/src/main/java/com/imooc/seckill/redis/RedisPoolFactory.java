package com.imooc.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by ky on 2018/9/9.
 */
@Service
public class RedisPoolFactory {
    @Autowired
    RedisConfig config;
    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        poolConfig.setMaxTotal(config.getPoolMaxTotal());
        poolConfig.setMaxIdle(config.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(config.getPoolMaxWait()*1000);//单位为毫秒
        //最后一个参数database是由于redis支持多种数据库,若不指定则从0开始,即支持所有的数据库
        return new JedisPool(poolConfig,config.getHost(),config.getPort(),config.getTimeout()*1000,config.getPassword(),0);
    }
}
