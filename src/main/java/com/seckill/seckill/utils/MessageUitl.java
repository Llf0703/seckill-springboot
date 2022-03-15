package com.seckill.seckill.utils;

import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

@Data
public class MessageUitl {
    private HashMap<String, Object> map;

    public void init(int code, String message, boolean status) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        map = new HashMap<String, Object>();
        map.put("time", fmt.format(date));
        map.put("timestamp", date.getTime());
        map.put("code", code);
        map.put("message", message);
        map.put("status", status);
        map.put("data", null);
    }

    public void init(int code, String message, boolean status, HashMap<String, Object> data) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        map = new HashMap<String, Object>();
        map.put("time", fmt.format(date));
        map.put("timestamp", date.getTime());
        map.put("code", code);
        map.put("message", message);
        map.put("status", status);
        map.put("data", data);
    }

    public void add_data(HashMap<String, Object> data) {
        map.put("data", data);
    }
}
