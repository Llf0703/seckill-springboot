package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
import com.seckill.seckill_manager.controller.vo.QueryByNameVO;
import com.seckill.seckill_manager.controller.vo.SeckillItemVO;
import com.seckill.seckill_manager.entity.SeckillItems;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
public interface ISeckillItemsService extends IService<SeckillItems> {
    Response editSeckillItem(SeckillItemVO itemVO);
    Response getSeckillItem(QueryByIdVO queryByIdVO);
    Response getSeckillItemPage(PageVO pageVO);
    Response deleteSeckillItemPage(QueryByIdVO queryByIdVO);
    Response searchFinancialItemOptions(QueryByNameVO queryByNameVO);
}
