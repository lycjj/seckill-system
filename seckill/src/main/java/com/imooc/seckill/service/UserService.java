package com.imooc.seckill.service;

import com.imooc.seckill.dao.UserDao;
import com.imooc.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ky on 2018/9/9.
 */
@Service
public class UserService {
    @Autowired
     UserDao dao;
    public User getById(Integer id){
        return dao.getById(id);
    }
    @Transactional
    public boolean tx(){
        User u1=new User();
        u1.setId(2);
        u1.setName("cjj");
        dao.insert(u1);
        User u2=new User();
        u2.setId(1);
        u2.setName("lucy");
        dao.insert(u2);
        return true;
    }
}
