package com.imooc.seckill.controller;


import com.imooc.seckill.Vo.GoodsDetailVo;
import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.redis.GoodsKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.VariablesMap;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * Created by ky on 2018/9/11.
 */
@Controller
@RequestMapping(value = "/seckill")
public class GoodsController {
    @Autowired
    SeckillUserService service;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    @Autowired
    ApplicationContext context;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @RequestMapping(value ="/goods/to_list",produces = "text/html")
    @ResponseBody
    //由于每次访问页面的时候都要做通过token从redis中获取用户信息的操作,这样每个都写特别麻烦,所以为了减少麻烦,采取另一种方式
    public String toList(HttpServletRequest request,HttpServletResponse response, Model model, SeckillUser user){
            //使用页面缓存改进程序
        //给页面缓存的key创建一个前缀
        model.addAttribute("user",user);
String html=redisService.get(GoodsKey.getGoodsList,"",String.class);
if(!StringUtils.isEmpty(html)){
    return html;
}
//手动渲染
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        SpringWebContext cxt=new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),context);
//渲染通过这个类
html= thymeleafViewResolver.getTemplateEngine().process("goods_list",cxt);
if(!StringUtils.isEmpty(html))
redisService.set(GoodsKey.getGoodsList,"",html);
        return html;

    }
    //在实际的互联网公司中商品id不会自增,也不会采用uuid,而是采用snowflake这个工具
    @RequestMapping(value = "/goods/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(SeckillUser user, @PathVariable("goodsId") long goodsId){
        //使用URL缓存改进秒杀详情页
        /*model.addAttribute("user",user);
        String html=redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }*/
        //使用页面静态化改进商品详情
        GoodsVo goodsVo=goodsService.getGoodsVoByGoodsId(goodsId);
        Long start=goodsVo.getStartDate().getTime();
        Long end=goodsVo.getEndDate().getTime();
        Long now=System.currentTimeMillis();
        int remainSecond;
        int status;
        if(now<start){
         status=0;
         remainSecond=(int)(start-now);
        }else if(now>end){
            status=2;
            remainSecond=-1;
        }else{
            status=1;
            remainSecond=0;
        }
        GoodsDetailVo goodsDetail=new GoodsDetailVo();
        goodsDetail.setGoods(goodsVo);
        goodsDetail.setUser(user);
        goodsDetail.setRemainSecond(remainSecond);
        goodsDetail.setStatus(status);
        return Result.success(goodsDetail);
       /* model.addAttribute("goodsVo",goodsVo);
        model.addAttribute("status",status);
        model.addAttribute("remainSeconds",remainSecond);
        SpringWebContext cxt=new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),context);
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",cxt);
        if(!StringUtils.isEmpty(html))
            redisService.set(GoodsKey.getGoodsList,""+goodsId,html);
        return html;*/
    }
}
