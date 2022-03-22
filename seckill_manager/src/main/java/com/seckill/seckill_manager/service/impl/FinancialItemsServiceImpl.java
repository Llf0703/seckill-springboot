package com.seckill.seckill_manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.entity.FinancialItems;
import com.seckill.seckill_manager.mapper.FinancialItemsMapper;
import com.seckill.seckill_manager.service.IFinancialItemsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
@Service
public class FinancialItemsServiceImpl extends ServiceImpl<FinancialItemsMapper, FinancialItems> implements IFinancialItemsService {
    @Resource
    private FinancialItemsMapper financialItemsMapper;
    @Override
    public Response editFinancialItem(FinancialItemVO financialItemVO){
        return Response.success("保存成功");
    }
}
