package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Interceptor.Type.OperateRecord;
import com.seckill.seckill_manager.common.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName OperateRecordInterceptor
 * @description: 用于日志记录
 * @date 2022/4/4 18:37
 */
@ControllerAdvice
public class OperateRecordGeneratorInterceptor implements ResponseBodyAdvice<Response> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //这个地方如果返回false ,不会执行 beforeBodyWrite 方法
        return true;
    }

    @Override
    public Response beforeBodyWrite(Response o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        OperateRecord operateRecord = methodParameter.getMethodAnnotation(OperateRecord.class);

        if (operateRecord != null) {
            System.out.println(operateRecord.operateName());
        }
        System.out.println("body");
        return o;
    }

}
