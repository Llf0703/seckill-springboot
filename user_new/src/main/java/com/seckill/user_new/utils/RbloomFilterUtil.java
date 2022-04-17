package com.seckill.user_new.utils;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RbloomFilterUtil {
    private static RedissonClient redissonClient;
    public static RBloomFilter<String> seckillBloomFilter;
    public static String seckillBloomFilterName = "U:bloomFilter:seckillItem";

    @Autowired
    public void init(RedissonClient redissonClient) {
        RbloomFilterUtil.redissonClient = redissonClient;
        RbloomFilterUtil.seckillBloomFilter = redissonClient.getBloomFilter(seckillBloomFilterName);
        bloomFilterInit();
    }

    public static void bloomFilterInit() {
        seckillBloomFilter.tryInit(1000, 0.01);
        Integer id = 1;
        while (id <= 1000) {
            bloomFilterAdd(seckillBloomFilterName, id.toString());
            id++;
        }
    }


    public static void bloomFilterAdd(String bloomFilterName, String value) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        bloomFilter.add(value);
    }

    public static boolean bloomFilterContains(String bloomFilterName, String value) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        return bloomFilter.contains(value);
    }

}
