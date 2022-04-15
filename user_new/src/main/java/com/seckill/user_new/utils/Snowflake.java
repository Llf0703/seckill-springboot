package com.seckill.user_new.utils;

public class Snowflake {
    private static final cn.hutool.core.lang.Snowflake snowFlakeWorker=new cn.hutool.core.lang.Snowflake(0);

    public static String nextID(){
        return snowFlakeWorker.nextIdStr();
    }
    public static Long nextLongID(){
        return snowFlakeWorker.nextId();
    }
}
