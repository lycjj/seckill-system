package com.imooc.seckill.result;

/**
 * Created by ky on 2018/9/8.
 */
//用于封装结果集
public class Result<T> {
    //返回给前端的状态码
    private int code;
    //状态码描述信息
    private String msg;
    //返回给前端的数据
    private T data;
//不希望用户去调用构造函数,success时调用的构造函数
    private Result(T data) {
        this.code=0;
        this.msg="success";
        this.data=data;
    }
//失败时调用的构造函数,没有返回值,只有code和msg
    private Result(CodeMsg cm){
        if(cm==null){
            return;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public static<T> Result<T> success(T data){
       return new Result<T>(data);
    }
    public static<T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }
}
