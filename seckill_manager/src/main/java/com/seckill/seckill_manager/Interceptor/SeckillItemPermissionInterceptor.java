package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Exception.InterceptorJWTException;
import com.seckill.seckill_manager.Interceptor.Type.SeckillItemPermission;
import com.seckill.seckill_manager.entity.ManagerUsers;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SeckillItemPermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;// 把handler强转为HandlerMethod
            SeckillItemPermission seckillItemPermission = handlerMethod.getMethod().getAnnotation(SeckillItemPermission.class);
            if (seckillItemPermission != null) {//进行鉴权
                ManagerUsers user = (ManagerUsers) request.getAttribute("user");
                boolean res = InterceptorUtils.seckillItemPermission(user, seckillItemPermission.permission());
                if (!res)
                    throw new InterceptorJWTException("权限不足");
            }
        }
        return true;
    }
}
