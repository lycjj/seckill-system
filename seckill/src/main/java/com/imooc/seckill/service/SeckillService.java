package com.imooc.seckill.service;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.domain.SeckillOrder;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.redis.SeckillKey;
import com.imooc.seckill.utils.MD5Util;
import com.imooc.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by ky on 2018/9/12.
 */
@Service
public class SeckillService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;
    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
        //减库存
       boolean isSuccess= goodsService.reduceStock(goods);
        //下订单
        if(isSuccess) {
            return orderService.createOrder(user, goods);
        }else{
            //若不成功,做个标记
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(Long id) {
         redisService.set(SeckillKey.GOODS_OVER,""+id,true);
    }

    private boolean getGoodsOver(Long id) {
        String prefix=SeckillKey.GOODS_OVER.getPrefix();
        String realKey=prefix+id;
          return redisService.exitKey(realKey);
    }
    public long getSeckillResult(Long userId, Long goodsId) {
        SeckillOrder order=orderService.getSeckillOrderByUserIdAndGoodsId(userId,goodsId);
       if(order!=null){
           return order.getOrderId();
       }else{
           //可能是还在轮询,也可能是秒杀失败
           //若秒杀失败,则说明库存不足
           if(getGoodsOver(goodsId)){
               return -1;
           }else{
               return 0;
           }
       }
    }

    public String createSeckillPath(long id, long goodsId) {
        String path= MD5Util.md5(UUIDUtil.uuid()+"123456");
        return path;
    }

    public boolean checkSeckillPath(Long id, Long goodsId, String path) {
        if(path==null || id==null || goodsId==null){
            return false;
        }
        String oldPath=redisService.get(SeckillKey.GET_SECKILL_PATH,id+"_"+goodsId,String.class);
        return path.equals(oldPath);
    }

    public BufferedImage createVerifyCode(SeckillUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.GET_SECKILL_VERIFY_CODE, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private int calc(String verifyCode) {
        try{
            ScriptEngineManager manager=new ScriptEngineManager();
            ScriptEngine engine= manager.getEngineByName("JavaScript");
            return (int)engine.eval(verifyCode);

        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private  char[] opts={'+','-','*'};
    private  String generateVerifyCode(Random rdm) {
        int num1=rdm.nextInt(10);
        int num2=rdm.nextInt(10);
        int num3=rdm.nextInt(10);
        char opt1=opts[rdm.nextInt(3)];
        char opt2=opts[rdm.nextInt(3)];
        String exp=""+num1+opt1+num2+opt2+num3;
        return exp;
    }

    public boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode) {
        if(user==null||goodsId<=0){
            return false;
        }
        Integer codeOld=redisService.get(SeckillKey.GET_SECKILL_VERIFY_CODE,user.getId()+","+goodsId,Integer.class);
        if(codeOld==null || codeOld-verifyCode!=0){
            return false;
        }
        redisService.delete(SeckillKey.GET_SECKILL_VERIFY_CODE,user.getId()+","+goodsId);
        return true;
    }
}
