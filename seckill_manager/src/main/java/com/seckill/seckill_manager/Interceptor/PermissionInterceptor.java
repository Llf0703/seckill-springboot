package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Exception.InterceptorJWTException;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.entity.ManagerUsers;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;// 把handler强转为HandlerMethod
            // 从handlerMethod中获取本次请求的接口方法对象然后判断该方法上是否标有我们自定义的注解@LoginRequired
            Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
            if (permission != null) {//进行鉴权
                boolean res = false;
                ManagerUsers user = (ManagerUsers) request.getAttribute("user");
                switch (permission.permission()) {
                    case PermissionType.FinancialItemPermission:
                        res = InterceptorUtils.financialItemPermission(user, permission.level());
                        break;
                    case PermissionType.SeckillItemPermission:
                        res = InterceptorUtils.seckillItemPermission(user, permission.level());
                        break;
                    case PermissionType.SeckillRecordPermission:
                        res = InterceptorUtils.seckilRecordPermission(user, permission.level());
                        break;
                    case PermissionType.AdminInfoPermission:
                        res = InterceptorUtils.adminInfoPermission(user, permission.level());
                        break;
                    case PermissionType.GuestInfoPermission:
                        res = InterceptorUtils.guestInfoPermission(user, permission.level());
                        break;
                    case PermissionType.RechargeRecordPermission:
                        res = InterceptorUtils.rechargeRecordPermission(user, permission.level());
                        break;
                    case PermissionType.RiskControlPermission:
                        res = InterceptorUtils.riskControlPermission(user, permission.level());
                        break;
                }
                if (!res) throw new InterceptorJWTException("权限不足");
            }
        }
        return true;
    }
}
