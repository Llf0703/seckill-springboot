package com.seckill.seckill_manager.config;

import com.seckill.seckill_manager.Interceptor.JWTInterceptor;
import com.seckill.seckill_manager.Interceptor.OperateRecordSaveInterceptor;
import com.seckill.seckill_manager.Interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private JWTInterceptor jwtInterceptor;
    @Resource
    private PermissionInterceptor permissionInterceptor;
    @Resource
    private OperateRecordSaveInterceptor operateRecordGeneratorInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(operateRecordGeneratorInterceptor).addPathPatterns("/**");
    }
}
