package com.seckill.seckill_manager.Interceptor.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @ClassName EditFinancialItemPermission
 * @description:  需要编辑理财产品权限注解
 * @author Wky1742095859
 * @date 2022/3/23 2:45
 * @version 1.0
 */
@Target({ElementType.METHOD})//表示可用在方法名上
@Retention(RetentionPolicy.RUNTIME)//表示在运行时有效
public @interface EditFinancialItemPermission {
}
