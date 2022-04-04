package com.seckill.seckill_manager.Interceptor;

import com.seckill.seckill_manager.Interceptor.Type.OperateRecord;
import com.seckill.seckill_manager.entity.ManagerUsers;
import com.seckill.seckill_manager.mapper.OperateRecordMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName OperateRecordInterceptor
 * @description: 日志拦截器
 * @date 2022/4/5 0:18
 */
@Component
public class OperateRecordInterceptor implements HandlerInterceptor {
    @Resource
    private OperateRecordMapper operateRecordMapper;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            OperateRecord operateRecord = handlerMethod.getMethod().getAnnotation(OperateRecord.class);
            if (operateRecord != null) {
                ManagerUsers user = (ManagerUsers) request.getAttribute("user");
                int operateId = (int) request.getAttribute("operateId");
                com.seckill.seckill_manager.entity.OperateRecord record = new com.seckill.seckill_manager.entity.OperateRecord();
                LocalDateTime nowTime = LocalDateTime.now();
                record.setUpdatedAt(nowTime);
                record.setOperate(operateRecord.operateName());
                record.setCreatedAt(nowTime);
                record.setManagerUserId(user.getId());
                record.setLevel(operateRecord.level());
                record.setOperateId(operateId);
                record.setManagerUserAccount(user.getAccount());
                try {
                    int res = operateRecordMapper.insert(record);
                } catch (Exception ignored) {
                }
            }
        }

    }
}
