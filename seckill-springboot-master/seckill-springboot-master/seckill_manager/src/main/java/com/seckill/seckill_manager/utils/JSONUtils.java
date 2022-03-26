package com.seckill.seckill_manager.utils;

import com.alibaba.fastjson.JSON;

public class JSONUtils {
    public static String toJSONStr(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T toEntity(String str, Class<T> clazz) {
        try {
            return JSON.parseObject(str, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
