package com.seckill.seckill_manager;

import com.seckill.seckill_manager.mapper.GoodsMapper;
import com.seckill.seckill_manager.mapper.TestMapper;
import com.seckill.seckill_manager.service.impl.TestServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class SeckillManagerApplicationTests {
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private TestMapper testMapper;
    @Resource
    private TestServiceImpl testService;
    @Test
    void contextLoads() throws Exception {
        testService.test();
    }

}
