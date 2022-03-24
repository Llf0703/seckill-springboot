package com.seckill.seckill_manager.Interceptor.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @ClassName Permission
 * @description:  权限注解
 * @author Wky1742095859
 * @date 2022/3/25 2:27
 * @version 1.0
 */
@Target({ElementType.METHOD})//表示可用在方法名上
@Retention(RetentionPolicy.RUNTIME)//表示在运行时有效
public @interface Permission {
    int level();
    int permission();
}
