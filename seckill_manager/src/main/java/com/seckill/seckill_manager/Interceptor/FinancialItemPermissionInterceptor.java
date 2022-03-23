package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Exception.InterceptorJWTException;
import com.seckill.seckill_manager.Interceptor.Type.FinancialItemPermission;
import com.seckill.seckill_manager.entity.ManagerUsers;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName EditFinancialItemPermissionInterceptor
 * @description: 拦截是否有权限修改理财产品
 * @date 2022/3/23 2:47
 */
@Component
public class FinancialItemPermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;// 把handler强转为HandlerMethod
            FinancialItemPermission financialItemPermission = handlerMethod.getMethod().getAnnotation(FinancialItemPermission.class);
            if (financialItemPermission != null) {//进行鉴权
                ManagerUsers user = (ManagerUsers) request.getAttribute("user");
                boolean res = InterceptorUtils.financialItemPermission(user, financialItemPermission.permission());
                if (!res)
                    throw new InterceptorJWTException("权限不足");
            }
        }
        return true;
    }
}