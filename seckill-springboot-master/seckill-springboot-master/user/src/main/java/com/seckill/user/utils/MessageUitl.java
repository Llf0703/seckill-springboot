package com.seckill.user.utils;

import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

interface Code {
    int SUCCESS = 200;//成功
    int AUTH_ERR = 401;//权限不足
    int SYSTEM_ERR = 500;//系统异常
}

@Data
public class MessageUitl {
    private HashMap<String, Object> map;

    public void success(String message) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        map = new HashMap<String, Object>();
        map.put("time", fmt.format(date));
        map.put("timestamp", date.getTime());
        map.put("code", Code.SUCCESS);
        map.put("message", message);
        map.put("status", true);
        map.put("data", null);
    }

    public void success(String message, Object data) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        map = new HashMap<String, Object>();
        map.put("time", fmt.format(date));
        map.put("timestamp", date.getTime());
        map.put("code", Code.SUCCESS);
        map.put("message", message);
        map.put("status", true);
        map.put("data", data);
    }

    public void auth_error(String message) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        map = new HashMap<String, Object>();
        map.put("time", fmt.format(date));
        map.put("timestamp", date.getTime());
        map.put("code", Code.AUTH_ERR);
        map.put("message", message);
        map.put("status", false);
        map.put("data", null);
    }

    public void system_error(String message) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        map = new HashMap<String, Object>();
        map.put("time", fmt.format(date));
        map.put("timestamp", date.getTime());
        map.put("code", Code.SYSTEM_ERR);
        map.put("message", message);
        map.put("status", false);
        map.put("data", null);
    }

    public void add_data(Object data) {
        map.put("data", data);
    }
}
