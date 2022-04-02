package com.seckill.seckill_manager;

import com.seckill.seckill_manager.mapper.GoodsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class SeckillManagerApplicationTests {
    @Resource
    private GoodsMapper goodsMapper;

    @Test
    void contextLoads() throws Exception {
    }

}
