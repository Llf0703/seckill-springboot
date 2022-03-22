package com.seckill.seckill_manager.Exception;

import lombok.Getter;

@Getter
public class InterceptorJWTException extends RuntimeException {
    private final String msg;

    public InterceptorJWTException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
