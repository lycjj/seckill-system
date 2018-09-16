package com.imooc.seckill.dao;

import com.imooc.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by ky on 2018/9/9.
 */
@Mapper
public interface UserDao
{

    @Select("select * from user where id= #{id}")
    public User getById(@Param("id") Integer id);
    @Insert("insert into user values(#{id},#{name})")
    public int insert(User user);
}
