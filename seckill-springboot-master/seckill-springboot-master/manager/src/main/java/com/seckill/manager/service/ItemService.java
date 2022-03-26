package com.seckill.manager.service;

import java.util.HashMap;

import com.seckill.manager.controller.vo.ItemVO;

public interface ItemService {
    public HashMap<String, Object> add_item_service(ItemVO item_VO, String token);
}
