package com.imooc.seckill.dao;

import com.imooc.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by ky on 2018/9/10.
 */
@Mapper
public interface SeckillUserDao {
    @Select("select * from seckill_user where id=#{id}")
    public SeckillUser getById(@Param("id") Long id);
}
