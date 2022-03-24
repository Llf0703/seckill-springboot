package com.seckill.seckill_manager.Exception;

import lombok.Getter;

/**
 * @ClassName InterceptorJWTException
 * @description:  捕获权限验证失败
 * @author Wky1742095859
 * @date 2022/3/25 2:27
 * @version 1.0
 */
@Getter
public class InterceptorJWTException extends RuntimeException {
    private final String msg;

    public InterceptorJWTException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
