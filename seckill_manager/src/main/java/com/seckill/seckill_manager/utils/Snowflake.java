package com.seckill.seckill_manager.utils;

public class Snowflake {
    private static final cn.hutool.core.lang.Snowflake snowFlakeWorker=new cn.hutool.core.lang.Snowflake(0);

    public static String nextID(){
        return snowFlakeWorker.nextIdStr();
    }
}
