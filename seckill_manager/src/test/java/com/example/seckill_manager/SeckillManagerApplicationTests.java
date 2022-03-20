package com.example.seckill_manager;

import com.example.seckill_manager.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SeckillManagerApplicationTests {



    @Test
    void contextLoads() {
        System.out.println(RedisUtils.del("test321123","test123123"));
        System.out.println(RedisUtils.set("test321123","恶趣味"));
        System.out.println(RedisUtils.set("test123123","恶趣味"));
        System.out.println(RedisUtils.del("test321123","test123123"));
    }

}
