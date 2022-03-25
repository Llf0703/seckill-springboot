package com.seckill.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.seckill.user.entity.Item;
import com.seckill.user.entity.Items;
import com.seckill.user.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

// Controller是SpringBoot里最基本的组件，
// 他的作用是把用户提交来的请求通过对URL的匹配，分配个不同的接收器，再进行处理，然后向用户返回结果。
// 他的重点就在于如何从HTTP请求中获得信息，提取参数，并分发给不同的处理服务。

@Slf4j
@RestController
@RequestMapping("/item_request")
public class ItemController {

    @Resource
    public DTO dto;
    @Resource
    public ItemService itemService;
    @GetMapping("/sorted_list/{order}")
    public JSONObject sort_controller(@PathVariable("order") int order) {
        switch (order) {
            case 1 -> {
                List<Items> list = itemService.findAll();
                JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
                jsonObject.put("data", list);
                return jsonObject;
            }
            case 2 -> {
                List<Items> list = dto.TimeComparator_1();
                JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
                jsonObject.put("data", list);
                return jsonObject;
            }
            case 3 -> {
                List<Items> list = dto.TimeComparator_2();
                JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
                jsonObject.put("data", list);
                return jsonObject;
            }
            case 4 -> {
                List<Items> list = dto.TimeComparator_3();
                JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
                jsonObject.put("data", list);
                return jsonObject;
            }
        }
        return null;
    }

    @PostMapping("/query_one")
    public JSONObject findOne_controller(@RequestParam int id){
       Item item = itemService.findById(id);
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("data",item);
       return jsonObject;
    }
}