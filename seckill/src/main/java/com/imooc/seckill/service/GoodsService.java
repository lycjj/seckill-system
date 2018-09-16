package com.imooc.seckill.service;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.dao.GoodsDao;
import com.imooc.seckill.domain.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ky on 2018/9/11.
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao dao;
    public List<GoodsVo> listGoodsVo(){
        return dao.listGoodsVo();
    }
    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return dao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g=new SeckillGoods();
        g.setGoodsId(goods.getId());
        int count=dao.reduceStock(g);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
}
