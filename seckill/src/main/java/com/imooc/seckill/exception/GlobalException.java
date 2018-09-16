package com.imooc.seckill.exception;

import com.imooc.seckill.result.CodeMsg;

/**
 * Created by ky on 2018/9/10.
 */
public class GlobalException extends RuntimeException{
    private CodeMsg msg;
    public GlobalException(CodeMsg msg){
        super(msg.toString());
        this.msg=msg;
    }

    public CodeMsg getMsg() {
        return msg;
    }
}
