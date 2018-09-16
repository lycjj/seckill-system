package com.imooc.seckill.service;
import com.imooc.seckill.Vo.LoginVo;
import com.imooc.seckill.dao.SeckillUserDao;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.exception.GlobalException;
import com.imooc.seckill.redis.RedisService;
import com.imooc.seckill.redis.SeckillUserKey;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.utils.MD5Util;
import com.imooc.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ky on 2018/9/10.
 */
@Service
public class SeckillUserService {
    @Autowired
    RedisService service;
    @Autowired
    SeckillUserDao dao;
    public static final String COOKI_NAME_TOKEN="token";
    public SeckillUser getById(Long id){
        return dao.getById(id);
    }
    //由于一般都是让方法返回具有意义的值,比如login方法只需要返回是登录成功还是失败即可,所以定义了一个全局异常
    public boolean login(HttpServletResponse response,LoginVo vo) throws GlobalException{
        if(vo==null){
            throw new GlobalException(CodeMsg.RESEVER_Error);
        }
        String mobile=vo.getMobile();
        String password=vo.getPassword();
        //判断手机号是否存在
        SeckillUser user= getById(Long.parseLong(mobile));
if(user==null){
    throw new GlobalException(CodeMsg.MOBILE_NOT_EXIT);
}
String dbPass=user.getPassword();
String salt=user.getSalt();
String formPass=vo.getPassword();
String pass= MD5Util.formPassToDbPass(formPass,salt);
//若两个密码相等,要用equals
if(!pass.equals(dbPass)){
    throw new GlobalException(CodeMsg.PASSWORD_ERROR);
}
//若登录成功
        String token= UUIDUtil.uuid();
        addCookie(response,token,user);
return true;
    }
    public SeckillUser getByToken(HttpServletResponse response,String token){
        if(token==null){
            return null;
        }
        SeckillUser user= service.get(SeckillUserKey.token,token,SeckillUser.class);
        //延长有效期
        if(user!=null) {
            addCookie(response,token,user);
        }
        return user;
    }
    private void addCookie(HttpServletResponse response,String token,SeckillUser user){
        service.set(SeckillUserKey.token,token,user);
//创建cookie
        Cookie cookie=new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
