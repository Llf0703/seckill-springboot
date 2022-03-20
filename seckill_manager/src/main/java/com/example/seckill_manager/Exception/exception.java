package com.example.seckill_manager.Exception;

import com.example.seckill_manager.common.Response;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName exception
 * @description: 全局异常处理
 * @date 2022/3/19 15:41
 */
@ControllerAdvice
public class exception {
    /*
     * @MethodName jsonExceptionHandler
     * @author Wky1742095859
     * @Description 处理json解析异常
     * @Date 2022/3/19 4:24
     * @Param [e]
     * @Return com.example.seckill_manager.common.Response
     **/
    @ExceptionHandler(value = JsonParseException.class)
    public @ResponseBody
    Response jsonExceptionHandler(JsonParseException e) {
        return Response.paramsErr("参数异常");
    }

    /*
     * @MethodName jsonExceptionHandler
     * @author Wky1742095859
     * @Description 处理json缺失异常
     * @Date 2022/3/19 4:24
     * @Param [e]
     * @Return com.example.seckill_manager.common.Response
     **/
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public @ResponseBody
    Response jsonExceptionHandler(HttpMessageNotReadableException e) {
        return Response.paramsErr("参数异常");
    }
    /*
     * @MethodName jsonExceptionHandler
     * @author Wky1742095859
     * @Description 处理请求头参数缺失
     * @Date 2022/3/19 15:55
     * @Param [req, e]
     * @Return com.example.seckill_manager.common.Response
    **/
    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public @ResponseBody
    Response jsonExceptionHandler(HttpServletRequest req, MissingRequestHeaderException e) {
        return Response.paramsErr("参数异常");
    }
}
