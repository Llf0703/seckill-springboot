package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
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
    Response editSeckillItem(SeckillItemVO item_VO);
}
