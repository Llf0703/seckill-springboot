package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Exception.InterceptorJWTException;
import com.seckill.seckill_manager.Interceptor.Type.EditFinancialItemPermission;
import com.seckill.seckill_manager.entity.ManagerUsers;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @ClassName EditFinancialItemPermissionInterceptor
 * @description:  拦截是否有权限修改理财产品
 * @author Wky1742095859
 * @date 2022/3/23 2:47
 * @version 1.0
 */
@Component
public class EditFinancialItemPermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;// 把handler强转为HandlerMethod
            EditFinancialItemPermission editFinancialItemPermission = handlerMethod.getMethod().getAnnotation(EditFinancialItemPermission.class);
            if (editFinancialItemPermission != null) {//进行鉴权
                ManagerUsers user=(ManagerUsers) request.getAttribute("user");
                HashMap<String, Object> res = InterceptorUtils.editFinancialItemPermission(user);
                if (res.get("status").equals(false))
                    throw new InterceptorJWTException("权限不足");
            }
        }
        return true;
    }
}
