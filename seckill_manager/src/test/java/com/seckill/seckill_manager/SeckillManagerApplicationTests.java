package com.seckill.seckill_manager;

import com.seckill.seckill_manager.mapper.GoodsMapper;
import com.seckill.seckill_manager.utils.Captcha;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class SeckillManagerApplicationTests {
    @Resource
    private GoodsMapper goodsMapper;

    @Test
    void contextLoads() throws Exception {
        System.out.println(Captcha.getCircleCaptcha().get("code"));
        System.out.println(Captcha.getGifCaptcha());
        System.out.println(Captcha.getShearCaptcha());
    }

}
