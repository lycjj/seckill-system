package com.imooc.seckill.controller;

import com.imooc.seckill.domain.User;
import com.imooc.seckill.rabbitmq.MQSender;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.redis.UserKey;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ky on 2018/9/8.
 */
@Controller
@RequestMapping(value = "/seckill")
public class DemoController {
    @Autowired
    private UserService service;
    @Autowired
    private RedisService redis;
    @Autowired
    MQSender mqSender;
    @RequestMapping("/say")
    public String say(){
        return "hello";
    }
   @RequestMapping("/hello")
    @ResponseBody
    //rest api json输出 2、页面 template模板
    public Result<String> hello(){
        //由于根据不同的状态有不同的情况,比如成功时状态码为0,失败时状态码为50010,则每次都需要new 一个result
        //为了避免这种情况,虽然可以通过创建一个常量类,但是为了使得代码更优雅,则通过调用方法
        //这样就不用创建对象了
        //return Result.success("hello");
        //失败的时候
        return Result.error(CodeMsg.RESEVER_Error);

    }
    /*@RequestMapping(value = "/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello,imooc");
       return Result.success("hello imooc");
    }*/
    /*@RequestMapping(value = "/topicMq")
    @ResponseBody
    public Result<String> topicMq(){
        mqSender.sendTopic("hello,imooc");
        return Result.success("hello imooc");
    }*/
   /* @RequestMapping(value = "/fanoutMq")
    @ResponseBody
    public Result<String> fanoutMq(){
        mqSender.sendFanout("hello,imooc");
        return Result.success("hello imooc");
    }*/
    //header
   /* @RequestMapping(value = "/headerMq")
    @ResponseBody
    public Result<String> headerMq(){
        mqSender.sendHeader("hello,imooc");
        return Result.success("hello imooc");
    }*/
    @RequestMapping("/thymeleaf")
   public String thymeleaf(Model model){
       model.addAttribute("name","ly");
       return "hello";
   }
   @ResponseBody
   @RequestMapping(value="/db/get",method= RequestMethod.GET)
public Result<User> doGet(){
       User user=service.getById(1);
     return Result.success(user);
}
//测试事务
    @ResponseBody
    @RequestMapping(value = "/db/tx",method= RequestMethod.GET)
    public Result<Boolean> dbTx(){
    boolean isSuccess=service.tx();
    return Result.success(isSuccess);
    }
    //测试redis
    @RequestMapping(value = "/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User data=redis.get(UserKey.getById,""+5,User.class);
        return Result.success(data);
    }
    @RequestMapping(value = "/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        //如果我们就是使用常量,当有多个人开发的时候,很容易就会被覆盖掉,所以在每个key前面加入一个前缀,即通用缓存key封装
        User user=new User();
        user.setId(5);
        user.setName("5555");
        boolean result=redis.set(UserKey.getById,user.getId().toString(),user);
        return Result.success(result);
    }
}
