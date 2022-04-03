package com.seckill.user_new.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName JSONUtils
 * @description: entity与字符串互转处理类
 * @date 2022/3/25 2:33
 */
public class JSONUtils {
    /*
     * @MethodName toJSONStr
     * @author Wky1742095859
     * @Description entity转字符串
     * @Date 2022/3/25 2:33
     * @Param [obj]
     * @Return java.lang.String
     **/
    public static String toJSONStr(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * @MethodName toEntity
     * @author Wky1742095859
     * @Description 字符串转entity
     * @Date 2022/3/25 2:33
     * @Param [str, clazz]
     * @Return T
     **/
    public static <T> T toEntity(String str, Class<T> clazz) {
        try {
            return JSON.parseObject(str, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
