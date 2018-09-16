package com.imooc.seckill.controller;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.access.AccessLimit;
import com.imooc.seckill.domain.SeckillOrder;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.rabbitmq.MQSender;
import com.imooc.seckill.rabbitmq.SeckillMessage;
import com.imooc.seckill.redis.AccessKey;
import com.imooc.seckill.redis.GoodsKey;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.redis.SeckillKey;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import com.imooc.seckill.service.SeckillService;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ky on 2018/9/12.
 */
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController implements InitializingBean{
    private Map<Long,Boolean> localOverMap=new HashMap<Long,Boolean>();
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    SeckillService seckillService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;
    @RequestMapping(value = "/{path}/do_seckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSeckill(SeckillUser user, Long goodsId,@PathVariable("path") String path){
        //若用户还没有登录,跳转到登录页面
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //比较传入的path和redis中缓存的path,若相同,则继续秒杀
        boolean check= seckillService.checkSeckillPath(user.getId(),goodsId,path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEAGAL);
        }
        //若重复秒杀
        SeckillOrder order=orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //先从redis中预减库存
        String prefix=GoodsKey.getMiaoshaGoodsStock.getPrefix();
        String realKey=prefix+goodsId;
        //由于若库存已经小于0了,但是若此时还有一个秒杀请求,还是会访问redis,但是此时已经没必要再访问redis了,所有可以进一步优化
        if(localOverMap.get(goodsId)){
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        long stock=redisService.decr(realKey);
        //库存不足
        if(stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
         //若可以秒杀,即库存足并且没有秒杀订单
        //入队
        SeckillMessage message=new SeckillMessage();
        message.setGoodsId(goodsId);
        message.setUser(user);
       mqSender.sendSeckillMessage(message);
       return Result.success(0);
      /*  GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
         long stock=goods.getStockCount();
        //若库存不足
        if(stock<=0){
            return Result.error(CodeMsg.SECKILL_OVER);

        }
        //若重复秒杀
        SeckillOrder order=orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if(order!=null){
           return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //若可以秒杀,则需要执行减库存、下订单、写入秒杀订单
OrderInfo orderInfo=seckillService.seckill(user,goods);
        return Result.success(orderInfo);*/
    }
    @RequestMapping(value = "/result")
    @ResponseBody
    public Result<Long> result(SeckillUser user,Long goodsId){
         //如果订单存在
        Long result= seckillService.getSeckillResult(user.getId(),goodsId);
        //reusult大于0，说明秒杀成功,等于0,说明还在处理,小于0,说明秒杀失败
        return Result.success(result);
    }
    //隐藏秒杀地址
    @AccessLimit(seconds = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillPath(SeckillUser user, @RequestParam("goodsId") long goodsId, @RequestParam(value = "verifyCode",defaultValue = "0") int verifyCode, HttpServletRequest request){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //校验验证码
        boolean check=seckillService.checkVerifyCode(user,goodsId,verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEAGAL);
        }
        //获取path
        String path= seckillService.createSeckillPath(user.getId(),goodsId);
        //将path存入redis中
        redisService.set(SeckillKey.GET_SECKILL_PATH,user.getId()+"_"+goodsId,path);
        return Result.success(path);
    }
    @RequestMapping(value = "/verifycode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillVerifyCode(HttpServletResponse response, SeckillUser user, long goodsId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
           BufferedImage image=seckillService.createVerifyCode(user, goodsId);
        try {
           OutputStream out= response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            //由于图片已经由out输出,所以不需要返回了
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return Result.error(CodeMsg.SECKILL_FAILURE);
        }
    }
//在系统初始化时将商品的库存存入redis
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list= goodsService.listGoodsVo();
        if(list==null){
            return;
        }
        for(GoodsVo goods:list){
              redisService.set(GoodsKey.getMiaoshaGoodsStock,goods.getId()+"",goods.getStockCount());
              localOverMap.put(goods.getId(),false);
        }
    }
}
