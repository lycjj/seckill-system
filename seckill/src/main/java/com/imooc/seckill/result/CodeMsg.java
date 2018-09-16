package com.imooc.seckill.result;

import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * Created by ky on 2018/9/8.
 */
public class CodeMsg {
    private int code;
    private String msg;
    //这样定义的好处不仅是在调用的时候不用创建对象,还可以通过模块定义异常
    //通用异常5001xx
    //登录异常5002xx
    //秒杀异常5005xx
    //订单模块5004xx
public static CodeMsg SUCCESS=new CodeMsg(0,"success");
public static CodeMsg RESEVER_Error=new CodeMsg(500100,"error");
public static CodeMsg BIND_ERROR=new CodeMsg(500101, "参数校验异常：%s");
public static final CodeMsg REQUEST_ILLEAGAL=new CodeMsg(500102,"请求是非法的");
public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问太频繁！");
//登录异常
public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
public static CodeMsg MOBILE_EMPTY=new CodeMsg(500212, "手机号不能为空");
public static CodeMsg MOBILE_ERROR=new CodeMsg(500213,"手机号格式错误");
public static CodeMsg MOBILE_NOT_EXIT=new CodeMsg(500214, "手机号不存在");
public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
//订单异常5004xx
    public static final CodeMsg ORDER_NOT_EXIT=new CodeMsg(500400,"订单不存在");
//秒杀模块5005xxx
    public static final CodeMsg SECKILL_OVER=new CodeMsg(500500,"商品已秒杀完毕");
    public static final CodeMsg REPEAT_SECKILL=new CodeMsg(500501,"不能重复秒杀");
    public static final CodeMsg SECKILL_FAILURE=new CodeMsg(500502,"秒杀失败");
private CodeMsg(int code,String msg){
    this.code=code;
    this.msg=msg;
}
public CodeMsg fillArgs(Object... args){
    int code=this.code;
    String message=String.format(this.msg,args);
    return new CodeMsg(code,message);
}
    public int getCode() {
        return code;
    }
//不需要set方法防止用户修改
    /*public void setCode(int code) {
        this.code = code;
    }*/

    public String getMsg() {
        return msg;
    }

    /*public void setMsg(String msg) {
        this.msg = msg;
    }*/
}
