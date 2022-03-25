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
    void contextLoads() {
        /*
        long time1=System.currentTimeMillis();
        Page<Goods> page=new Page<>(490000,10);
        QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").orderByAsc("start_time");
        goodsMapper.selectPage(page,queryWrapper);
        long time2=System.currentTimeMillis();
        List<Goods> records=page.getRecords();
        System.out.println(time2-time1);
        System.out.println(page.getTotal());
        System.out.println(records);

         */
    }

}
