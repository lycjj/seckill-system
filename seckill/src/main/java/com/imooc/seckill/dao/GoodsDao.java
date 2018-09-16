package com.imooc.seckill.dao;

import com.imooc.seckill.Vo.GoodsVo;
import com.imooc.seckill.domain.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by ky on 2018/9/11.
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods g on sg.goods_id=g.id")
public List<GoodsVo> listGoodsVo();
    @Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods g on sg.goods_id=g.id where g.id=#{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(long goodsId);
@Update("update seckill_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    public int reduceStock(SeckillGoods g);
}
