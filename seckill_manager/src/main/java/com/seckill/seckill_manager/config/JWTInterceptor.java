package com.seckill.seckill_manager.config;

import com.example.seckill_manager.Exception.InterceptorException;
import com.seckill.seckill_manager.config.InterceptorAdapter.Interceptor;
import com.seckill.seckill_manager.config.InterceptorAdapter.LoginRequired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;// 把handler强转为HandlerMethod
            // 从handlerMethod中获取本次请求的接口方法对象然后判断该方法上是否标有我们自定义的注解@LoginRequired
            LoginRequired loginRequired = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
            if (loginRequired != null) {//进行鉴权
                if (!Interceptor.loginRequired(request.getHeader("token"), request.getHeader("X-real-ip")))
                    throw new InterceptorException("请先登录");
            }
        }
        return true;
    }


}
