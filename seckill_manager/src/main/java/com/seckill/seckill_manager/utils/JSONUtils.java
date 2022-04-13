package com.seckill.seckill_manager.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

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
    /*
     * @MethodName toEntity
     * @author Wky1742095859
     * @Description redis hash转entity
     * @Date 2022/4/13 22:57
     * @Param [map, clazz]
     * @Return T
    **/
    public static <T> T toEntity(Map<String,String> map,Class<T> clazz){
        try {
            return JSON.parseObject(JSON.toJSONString(map), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    /*
     * @MethodName toRedisHash
     * @author Wky1742095859
     * @Description entity转redis hash
     * @Date 2022/4/13 22:57
     * @Param [entity]
     * @Return java.util.Map<java.lang.String,java.lang.String>
    **/
    public static Map<String,String> toRedisHash(Object entity){
        try {
            return JSON.parseObject(JSON.toJSONString(entity),new TypeReference<Map<String,String>>(){});
        } catch (Exception e) {
            return null;
        }
    }
}
