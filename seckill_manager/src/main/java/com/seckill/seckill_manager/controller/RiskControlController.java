package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.RiskControlVO;
import com.seckill.seckill_manager.service.impl.RiskControlServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-24
 */
@Controller
@RequestMapping("/api/risk_control")
public class RiskControlController {
    @Resource
    private RiskControlServiceImpl riskControlService;
    @LoginRequired
    @Permission(level = LevelCode.EDIT,permission = PermissionType.RiskControlPermission)
    @PostMapping("/add_item")
    public Response addItem(@RequestBody RiskControlVO riskControlVO){
        return riskControlService.editRiskControl(riskControlVO);
    }
}
