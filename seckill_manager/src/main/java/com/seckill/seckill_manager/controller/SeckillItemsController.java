package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.SeckillItemVO;
import com.seckill.seckill_manager.service.impl.SeckillItemsServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
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
    private SeckillItemsServiceImpl seckillItemsService;

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.SeckillItemPermission)
    @PostMapping("/add_item")
    public Response add_item_controller(HttpServletRequest request, @RequestBody SeckillItemVO item) {
        return seckillItemsService.editSeckillItem(item);
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.SeckillItemPermission)
    @PostMapping("/edit_item")
    public Response editItemController(HttpServletRequest request, @RequestBody SeckillItemVO item) {
        return seckillItemsService.editSeckillItem(item);
    }
}
