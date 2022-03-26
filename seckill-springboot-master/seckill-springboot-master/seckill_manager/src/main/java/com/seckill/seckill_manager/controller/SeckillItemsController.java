package com.seckill.seckill_manager.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ItemVO;
import com.seckill.seckill_manager.service.ItemService;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
@CrossOrigin
@RestController
@RequestMapping("/api/seckill_item")
public class SeckillItemsController {
    @Resource
    private ItemService item_service;

    @PostMapping("/add_item")
    public Response add_item_controller(HttpServletRequest request, @RequestBody ItemVO item) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return item_service.add_item_service(item, token, ip);
    }
}
