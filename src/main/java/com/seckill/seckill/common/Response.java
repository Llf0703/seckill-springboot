package com.seckill.seckill.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

interface Code {
    int SUCCESS = 200;//成功
    int AUTH_ERR = 401;//权限不足
    int SYSTEM_ERR = 500;//系统异常
}


/**
 * 统一接口返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private int code;
    private boolean status;
    private String message;
    private Object data;
    private String time;
    private long timestamp;

    public static Response success(String msg) {//操作成功, 无data
        SimpleDateFormat timeFormat = new SimpleDateFormat();
        timeFormat.applyLocalizedPattern("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String nowTimeStr = timeFormat.format(nowTime);
        return new Response(Code.SUCCESS, true, msg, null, nowTimeStr, nowTime.getTime());
    }

    public static Response success(Object data, String msg) {//操作成功, 有data
        SimpleDateFormat timeFormat = new SimpleDateFormat();
        timeFormat.applyLocalizedPattern("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String nowTimeStr = timeFormat.format(nowTime);
        return new Response(Code.SUCCESS, true, msg, data, nowTimeStr, nowTime.getTime());
    }

    public static Response authErr(String msg) {//鉴权失败
        SimpleDateFormat timeFormat = new SimpleDateFormat();
        timeFormat.applyLocalizedPattern("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String nowTimeStr = timeFormat.format(nowTime);
        return new Response(Code.AUTH_ERR, true, msg, null, nowTimeStr, nowTime.getTime());
    }
}
