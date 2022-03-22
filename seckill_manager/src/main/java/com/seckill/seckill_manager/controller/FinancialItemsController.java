package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.Type.EditFinancialItemPermission;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.entity.ManagerUsers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @LoginRequired
    @EditFinancialItemPermission
    @PostMapping("/add_item")
    public Response test(HttpServletRequest request) {
        ManagerUsers managerUsers = (ManagerUsers) request.getAttribute("user");
        System.out.println(managerUsers);
        return Response.success("ok");
    }
}
