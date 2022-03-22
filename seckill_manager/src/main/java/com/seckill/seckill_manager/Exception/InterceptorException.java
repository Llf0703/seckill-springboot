package com.seckill.seckill_manager.Exception;

import lombok.Getter;

@Getter
public class InterceptorException extends RuntimeException {
    private final String msg;

    public InterceptorException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
