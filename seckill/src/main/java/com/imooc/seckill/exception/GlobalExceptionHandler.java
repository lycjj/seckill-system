package com.imooc.seckill.exception;

import com.imooc.seckill.result.CodeMsg;
import com.imooc.seckill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ky on 2018/9/10.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        //若是全局异常
        if(e instanceof GlobalException){
            GlobalException ex=(GlobalException) e;
            return Result.error(ex.getMsg());
        }
        //如果是绑定异常
        if(e instanceof BindException){
            BindException ex=(BindException)e;
            //获取所有的错误信息
            List<ObjectError> errors=ex.getAllErrors();
            ObjectError arg=errors.get(0);
            //将错误信息作为参数放入错误提示中
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(arg));
        }else{
            return Result.error(CodeMsg.RESEVER_Error);
        }
    }
}
