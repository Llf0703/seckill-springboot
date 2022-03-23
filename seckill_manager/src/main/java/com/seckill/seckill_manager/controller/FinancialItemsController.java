package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.service.impl.FinancialItemsServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
@RestController
@RequestMapping("/api/financial_item")
public class FinancialItemsController {
    @Resource
    private FinancialItemsServiceImpl financialItemsService;

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.FinancialItemPermission)
    @PostMapping("/add_item")
    public Response addItem(@RequestBody FinancialItemVO financialItemVO) {
        return financialItemsService.editFinancialItem(financialItemVO);
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT,permission = PermissionType.FinancialItemPermission)
    @PostMapping("/edit_item")
    public Response editItem(@RequestBody FinancialItemVO financialItemVO) {
        return financialItemsService.editFinancialItem(financialItemVO);
    }
}
