package com.seckill.user_new.Exception;

import lombok.Getter;


@Getter
public class InterceptorSystemException extends RuntimeException {
    private final String msg;

    public InterceptorSystemException(String msg) {
        super(msg);
        this.msg = msg;
    }
}

