package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.entity.FinancialItems;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
public interface IFinancialItemsService extends IService<FinancialItems> {
    Response editFinancialItem(FinancialItemVO financialItemVO);
}
