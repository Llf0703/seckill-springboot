package com.seckill.user_new.controller;


import com.seckill.user_new.Interceptor.Type.LoginRequired;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.service.impl.SeckillItemsServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
@RestController
@RequestMapping("/api/seckill_items")
public class SeckillItemsController {
    @Resource
    private SeckillItemsServiceImpl seckillItemsService;

    @LoginRequired
    @PostMapping("/get_overview")
    public Response getOverview(@RequestBody PageVO pageVO) {
        return seckillItemsService.getOverview(pageVO);
    }

    @LoginRequired
    @PostMapping("/get_detail")
    public Response getDetail(@RequestBody QueryVO queryVO) {
        return seckillItemsService.getDetail(queryVO);
    }

    @LoginRequired
    @PostMapping("/get_seckill_link/{id}")
    public Response getSeckillLink(@PathVariable("id") String id) {
        return seckillItemsService.getSeckillLink(id);
    }

    @LoginRequired
    @PostMapping("/do_seckill_link/{uid}")
    public Response doSeckill(@PathVariable("uid") String uid) {
        return seckillItemsService.doSeckill(uid);
    }
}
