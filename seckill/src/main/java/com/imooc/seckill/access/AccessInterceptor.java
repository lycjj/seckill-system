package com.imooc.seckill.access;

import com.alibaba.fastjson.JSON;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.redis.AccessKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Created by ky on 2018/9/15.
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    SeckillUserService service;
    @Autowired
    RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         if(handler instanceof HandlerMethod) {
             HandlerMethod hm = (HandlerMethod) handler;
             AccessLimit limit = hm.getMethodAnnotation(AccessLimit.class);
             if (limit == null) {
                 return true;
             }
             //获取user并将它存储
             SeckillUser user = getUser(request, response);
             //存储
             SetContext.setUser(user);
             int seconds = limit.seconds();
             int maxCount = limit.maxCount();
             boolean needLogin = limit.needLogin();
             //如果需要登录
             if (needLogin) {
                 if (user == null) {
                     //回写信息
                     render(response, CodeMsg.SESSION_ERROR);
                     return false;
                 }
                 //接口限流,5秒钟只能点击5次,由于有多个方法都需要限流,所以如果在每个方法中都写上这些代码就比较繁琐,所以定义一个拦截器
                 String uri = request.getRequestURI();
                 AccessKey accessKey = AccessKey.withExpire(seconds);
                 Integer count = redisService.get(accessKey, uri + "_" + user.getId(), Integer.class);
                 if (count == null) {
                     //若count等于null,则说明是第一次访问,则在redis中存储1
                     redisService.set(accessKey, uri + "_" + user.getId(), 1);
                 } else if (count < maxCount) {
                     //若存在且小于5，则count++
                     String realKey = accessKey.getPrefix() + uri + "_" + user.getId();
                     redisService.incr(realKey);
                 } else {
                     render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                     return false;
                 }
             }
         }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        //将对象转成字符串
        String session= JSON.toJSONString(Result.error(sessionError));
        OutputStream out=response.getOutputStream();
        out.write(session.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
    private SeckillUser getUser(HttpServletRequest request,HttpServletResponse response){
        String paramValue=request.getParameter(SeckillUserService.COOKI_NAME_TOKEN);
        String cookieValue=getCookieValue(request,SeckillUserService.COOKI_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieValue) &&StringUtils.isEmpty(paramValue)){
            return null;

        }
//优先取paramValue
        String value=StringUtils.isEmpty(paramValue)?cookieValue:paramValue;
//通过token获取存储在redis中的user对象
        //有一个问题,在session中有效期是这样的,假设你10点访问了服务端,有效期为30分钟,那么到10:30你的session才过期,
        //但是若你在10.10分的时候又访问了服务端,那么你的session在10:40的时候C才过期,所以需要实现这个功能,改造getByToken()
        SeckillUser user=service.getByToken(response,value);
        return user;
    }
    public String getCookieValue(HttpServletRequest reuqest,String name){
        Cookie[] cookies=reuqest.getCookies();
        if(cookies==null||cookies.length<=0){
            return null;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
