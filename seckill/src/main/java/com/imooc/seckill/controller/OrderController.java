package com.imooc.seckill.controller;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.Vo.OrderDetailVo;
import com.imooc.seckill.domain.OrderInfo;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.GoodsService;
import com.imooc.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ky on 2018/9/13.
 */
@Controller
@RequestMapping(value = "/seckill")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;
    @RequestMapping(value = "order/detail/{orderId}")
    @ResponseBody
    public Result<OrderDetailVo> orderInfo(SeckillUser user, @PathVariable(value = "orderId") Long orderId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
     OrderInfo orderInfo= orderService.getOrderById(orderId);
        if(orderInfo==null){
            Result.error(CodeMsg.ORDER_NOT_EXIT);
        }
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo=new OrderDetailVo();
        orderDetailVo.setGoods(goods);
        orderDetailVo.setOrder(orderInfo);
        return Result.success(orderDetailVo);
    }
}
