package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Exception.InterceptorJWTException;
import com.seckill.seckill_manager.Interceptor.Type.SeckilRecordPermission;
import com.seckill.seckill_manager.entity.ManagerUsers;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
public class SeckillRecordInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;// 把handler强转为HandlerMethod
            SeckilRecordPermission seckilRecordPermission = handlerMethod.getMethod().getAnnotation(SeckilRecordPermission.class);
            if (seckilRecordPermission != null) {//进行鉴权
                ManagerUsers user=(ManagerUsers) request.getAttribute("user");
                HashMap<String, Object> res = InterceptorUtils.seckilRecordPermission(user, seckilRecordPermission.permission());
                if (res.get("status").equals(false))
                    throw new InterceptorJWTException("权限不足");
            }
        }
        return true;
    }
}
