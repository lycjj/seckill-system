package com.imooc.seckill.redis;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by ky on 2018/9/9.
 */
@Service
public class RedisService {
    @Autowired
  JedisPool jedisPool;
public<T> T get(KeyPrefix prefix,String key,Class<T> clazz){
   //获取jedis,通过jedisPool
    Jedis jedis=null;
    try{
        jedis=jedisPool.getResource();
        //将key和前缀做个拼接
        String realKey=prefix.getPrefix()+key;
        String value=jedis.get(realKey);
        //String——>bean 反序列化
        return stringToBean(value,clazz);
    }finally {
        returnToPool(jedis);
    }
}
//set方法
    public<T> boolean set(KeyPrefix prefix,String key,T bean){
    Jedis jedis=null;
    try{
        jedis=jedisPool.getResource();
        //由于jedis中set中的一个参数为字符串类型,所以要先将bean——>String 序列化
        String value=beanToString(bean);
        if(value==null ||value.length()<=0){
            return false;
        }
        String realKey=prefix.getPrefix()+key;
        //若设置的过期时间为0,则说明永不过期
        if(prefix.expireSeconds()<=0){
            jedis.set(realKey,value);
        }
        //否则调用redis中设置了存储值过期时间的方法
        else {
            jedis.setex(realKey, prefix.expireSeconds(), value);
        }
    }finally{
        returnToPool(jedis);
    }
    return true;
    }
    //key是否存在
    public boolean exitKey(String key){
       Jedis jedis=null;
       try{
           jedis=jedisPool.getResource();
          return jedis.exists(key);
       }finally{
           returnToPool(jedis);
       }
    }
    //incr 若value为整数 则增加
    public long incr(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.incr(key);
        }finally{
            returnToPool(jedis);
        }
    }
    //decr 减少
    public long decr(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.decr(key);
        }finally{
            returnToPool(jedis);
        }
    }
private void returnToPool(Jedis jedis){
    if(jedis!=null){
        //还给连接池,jedis才是pool中的连接
        jedis.close();
    }
}
public static<T>  T stringToBean(String value ,Class<T> clazz){
    if(value==null||value.length()<=0){
        return null;
    }
    else if(clazz==int.class||clazz==Integer.class){
           return (T)Integer.valueOf(value);
    }else if(clazz==long.class||clazz==Long.class){
        return (T)Long.valueOf(value);
    }else if(clazz==String.class){
        return (T)value;
    }else {
        return JSON.toJavaObject(JSON.parseObject(value),clazz);
    }
}
public static<T> String beanToString(T bean){
    if(bean==null){
        return null;
    }
    //若为int
    else if(bean.getClass()==int.class||bean.getClass()==Integer.class){
        return bean+"";

    }else if(bean.getClass()==long.class||bean.getClass()==Long.class){
                  return bean+"";
    }else if(bean.getClass()==String.class){
        return (String)bean;
    }else {
        return JSON.toJSONString(bean);
    }
}
public boolean delete(KeyPrefix prefix,String key){
    Jedis jedis=null;
    try {
        jedis=jedisPool.getResource();
        String realKey=prefix.getPrefix()+key;
        long rec=jedis.del(realKey);
         return rec>0;
    }finally {
        if(jedis!=null){
            returnToPool(jedis);
        }
    }
}
}
