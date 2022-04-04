package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.OperateRecord;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
import com.seckill.seckill_manager.controller.vo.QueryByNameVO;
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
    @OperateRecord(operateName = "编辑秒杀活动")
    @PostMapping("/edit_item")
    public Response editItemController(HttpServletRequest request, @RequestBody SeckillItemVO item) {
        Response res= seckillItemsService.editSeckillItem(item);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.SeckillItemPermission)
    @PostMapping("/search_financial_item")
    public Response searchFinancialItemOptions(HttpServletRequest request, @RequestBody QueryByNameVO queryByNameVO) {
        Response res= seckillItemsService.searchFinancialItemOptions(queryByNameVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.SeckillItemPermission)
    @PostMapping("/search_risk_control")
    public Response searchRiskControlOptions(HttpServletRequest request, @RequestBody QueryByNameVO queryByNameVO) {
        Response res= seckillItemsService.searchRiskControlOptions(queryByNameVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.SeckillItemPermission)
    @OperateRecord(operateName = "获取单个秒杀活动信息")
    @PostMapping("/get_item")
    public Response getItem(HttpServletRequest request, @RequestBody QueryByIdVO queryByIdVO) {
        Response res= seckillItemsService.getSeckillItem(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.SeckillItemPermission)
    @OperateRecord(operateName = "分页查询秒杀活动信息")
    @PostMapping("/get_page")
    public Response getPage(HttpServletRequest request, @RequestBody PageVO pageVO) {
        Response res= seckillItemsService.getSeckillItemPage(pageVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.SeckillItemPermission)
    @OperateRecord(operateName = "删除秒杀活动")
    @PostMapping("/delete_item")
    public Response deleteItem(HttpServletRequest request, QueryByIdVO queryByIdVO) {
        Response res= seckillItemsService.deleteSeckillItemPage(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }
}
