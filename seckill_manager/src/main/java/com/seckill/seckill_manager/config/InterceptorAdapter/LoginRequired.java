package com.seckill.seckill_manager.config.InterceptorAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})//表示可用在方法名上
@Retention(RetentionPolicy.RUNTIME)//表示在运行时有效
public @interface LoginRequired {
}
