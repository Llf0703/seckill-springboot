package com.example.seckill_manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.jar.JarEntry;


@SpringBootTest
class SeckillManagerApplicationTests {

    @Resource
    private JedisPool jedisPool;

    @Test
    void contextLoads() {

    }

}
