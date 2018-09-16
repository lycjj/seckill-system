package com.imooc.seckill.controller;

import com.imooc.seckill.Vo.LoginVo;
import com.imooc.seckill.domain.SeckillUser;
import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import com.imooc.seckill.service.SeckillUserService;
import com.imooc.seckill.utils.ValidatorUtil;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by ky on 2018/9/10.
 */
@Controller
@RequestMapping(value = "/seckill")
public class LoginController {
    @Autowired
    SeckillUserService service;
    private static Logger logger= LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(value = "/login/to_login")
public String toLogin(){
    return "login";
}
//vo用于接收请求中的参数
    @RequestMapping(value = "/login/do_login",method = RequestMethod.POST)
    @ResponseBody
public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo vo){
    //参数校验,这些参数校验代码都是没什么意义的,为了减少这些代码,使用JSR参数校验
        //1、导包spring-boot-validatior
/*String mobile=vo.getMobile();
String password=vo.getPassword();
if(StringUtils.isEmpty(password)){
return Result.error(CodeMsg.PASSWORD_EMPTY);
}
if(StringUtils.isEmpty(mobile)){
    return Result.error(CodeMsg.MOBILE_EMPTY);
}
if(!ValidatorUtil.isMobile(mobile)){
    return Result.error(CodeMsg.MOBILE_ERROR);
}*/
boolean cm=service.login(response,vo);
return Result.success(cm);
}
}
