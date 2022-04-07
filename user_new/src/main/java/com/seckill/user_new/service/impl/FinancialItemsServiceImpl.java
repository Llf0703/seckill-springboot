package com.seckill.user_new.service.impl;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.entity.FinancialItems;
import com.seckill.user_new.mapper.FinancialItemsMapper;
import com.seckill.user_new.service.IFinancialItemsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
@Service
public class FinancialItemsServiceImpl extends ServiceImpl<FinancialItemsMapper, FinancialItems> implements IFinancialItemsService {

    @Override
    public Response getDetail(QueryVO queryVO) {
        return null;
    }
}
