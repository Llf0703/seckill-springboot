package com.seckill.user_new.service;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.entity.SeckillItems;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
public interface ISeckillItemsService extends IService<SeckillItems> {
    Response getOverview(PageVO pageVO);
    Response getDetail(QueryVO queryVO);
}
