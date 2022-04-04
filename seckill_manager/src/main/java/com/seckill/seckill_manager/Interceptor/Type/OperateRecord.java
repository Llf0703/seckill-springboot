package com.seckill.seckill_manager.Interceptor.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName OperateRecord
 * @description:  日志记录注解
 * @author Wky1742095859
 * @date 2022/4/4 17:45
 * @version 1.0
 */

@Target({ElementType.METHOD})//表示可用在方法名上
@Retention(RetentionPolicy.RUNTIME)//表示在运行时有效
public @interface OperateRecord {
    String operateName();
    int level();
}
