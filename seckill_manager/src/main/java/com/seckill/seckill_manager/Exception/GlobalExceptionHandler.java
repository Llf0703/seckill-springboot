package com.seckill.seckill_manager.Exception;

import com.seckill.seckill_manager.common.Response;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName exception
 * @description: 全局异常处理
 * @date 2022/3/19 15:41
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    Response ExceptionHandler(Exception e) {
        e.printStackTrace();
        return Response.systemErr("系统异常");
    }

    /*
     * @MethodName ExceptionHandler404
     * @author Wky1742095859
     * @Description 通用异常处理
     * @Date 2022/4/4 19:00
     * @Param [e]
     * @Return com.seckill.seckill_manager.common.Response
     **/
    @ExceptionHandler(NoHandlerFoundException.class)
    public @ResponseBody
    Response ExceptionHandler404(NoHandlerFoundException e) {
        return Response.dataNotFoundErr("无效的接口");
    }

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

    /*
     * @MethodName JWTExceptionHandler
     * @author Wky1742095859
     * @Description 捕获权限验证失败
     * @Date 2022/3/25 2:27
     * @Param [req, e]
     * @Return com.seckill.seckill_manager.common.Response
     **/
    @ExceptionHandler(value = InterceptorJWTException.class)
    public @ResponseBody
    Response JWTExceptionHandler(HttpServletRequest req, InterceptorJWTException e) {
        return Response.authErr(e.getMsg());
    }

    @ExceptionHandler(value = InterceptorSystemException.class)
    public @ResponseBody
    Response SystemException(HttpServletRequest req, InterceptorSystemException e) {
        return Response.systemErr(e.getMsg());
    }
}
