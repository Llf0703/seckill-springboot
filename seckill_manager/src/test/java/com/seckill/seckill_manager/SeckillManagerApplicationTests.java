package com.seckill.seckill_manager;

import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.mapper.GoodsMapper;
import com.seckill.seckill_manager.utils.crypto.AESUtil;
import com.seckill.seckill_manager.utils.crypto.RSAUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class SeckillManagerApplicationTests {
    @Resource
    private GoodsMapper goodsMapper;

    @Test
    void contextLoads() throws Exception {
        Response test=Response.success("ok");
        String key=test.getTimeStamp()+"000";
        System.out.println(key);
        String cip=AESUtil.encrypt(RSAUtil.getPublicKey(),key);
        System.out.println(cip);
        System.out.println(AESUtil.decrypt(cip,key));
    }

}
