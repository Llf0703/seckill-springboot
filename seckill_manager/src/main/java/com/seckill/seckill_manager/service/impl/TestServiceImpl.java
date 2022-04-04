package com.seckill.seckill_manager.service.impl;

import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.entity.Test;
import com.seckill.seckill_manager.mapper.TestMapper;
import com.seckill.seckill_manager.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-04
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {
    @Resource
    private TestMapper testMapper;
    @Override
    public Response test() {
        Test test=new com.seckill.seckill_manager.entity.Test();
        test.setTest(new BigDecimal("1"));
        System.out.println(test.getId());
        boolean res=save(test);
        System.out.println(res);
        System.out.println(test.getId());
        return Response.success("oik",0);
    }
}
