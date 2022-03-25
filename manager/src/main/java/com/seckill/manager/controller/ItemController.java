package com.seckill.manager.controller;

import java.util.HashMap;

import javax.annotation.Resource;

import com.seckill.manager.controller.vo.ItemVO;
import com.seckill.manager.service.ItemService;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/manager/item")
public class ItemController {
    @Resource
    private ItemService item_service;

    @PostMapping("/add_item")
    public HashMap<String, Object> add_item_controller(@RequestBody ItemVO item, @RequestHeader("token") String token) {
        return item_service.add_item_service(item, token);
    }
}
