package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ItemVO;
import com.seckill.seckill_manager.entity.SeckillItems;

public interface ItemService extends IService<SeckillItems> {
    public Response add_item_service(ItemVO item_VO, String token, String ip);
}
