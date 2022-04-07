package com.seckill.user_new.service.impl;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.service.ISeckillItemsService;
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
public class SeckillItemsServiceImpl extends ServiceImpl<SeckillItemsMapper, SeckillItems> implements ISeckillItemsService {

    @Override
    public Response getOverview(PageVO pageVO) {
        return null;
    }

    @Override
    public Response getDetail(QueryVO queryVO) {
        return null;
    }
}
