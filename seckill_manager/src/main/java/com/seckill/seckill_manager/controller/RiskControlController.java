package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.OperateRecord;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.controller.vo.RiskControlVO;
import com.seckill.seckill_manager.service.impl.RiskControlServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-24
 */
@RestController
@RequestMapping("/api/risk_control")
public class RiskControlController {
    @Resource
    private RiskControlServiceImpl riskControlService;

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.RiskControlPermission)
    @OperateRecord(operateName = "编辑决策引擎",level = LevelCode.OPERATE_EDIT)
    @PostMapping("/edit_item")
    public Response editItem(HttpServletRequest request, @RequestBody RiskControlVO riskControlVO) {
        Response res = riskControlService.editRiskControl(riskControlVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.RiskControlPermission)
    @OperateRecord(operateName = "获取单个决策引擎信息",level = LevelCode.OPERATE_READ)
    @PostMapping("/get_item")
    public Response getItem(HttpServletRequest request, @RequestBody QueryVO queryByIdVO) {
        Response res = riskControlService.getRiskControl(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.RiskControlPermission)
    @OperateRecord(operateName = "分页查询决策引擎信息",level = LevelCode.OPERATE_READ)
    @PostMapping("/get_page")
    public Response getPage(HttpServletRequest request, @RequestBody PageVO pageVO) {
        Response res = riskControlService.getRiskControlPage(pageVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.RiskControlPermission)
    @OperateRecord(operateName = "删除决策引擎",level = LevelCode.OPERATE_DELETE)
    @PostMapping("/delete_item")
    public Response deleteItem(HttpServletRequest request, @RequestBody QueryVO queryByIdVO) {
        Response res = riskControlService.deleteRiskControl(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }
}
