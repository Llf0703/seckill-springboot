package com.seckill.user_new.controller;


import com.seckill.user_new.Interceptor.Type.LoginRequired;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.service.impl.SeckillItemsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
@Controller
@RequestMapping("/api/seckill_items")
public class SeckillItemsController {
    @Resource
    private SeckillItemsServiceImpl seckillItemsService;

    @LoginRequired
    @PostMapping("/get_overview")
    public Response getOverview(@RequestBody PageVO pageVO) {
        return seckillItemsService.getOverview(pageVO);
    }
}
