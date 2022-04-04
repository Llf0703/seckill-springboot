package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.OperateRecord;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.service.impl.FinancialItemsServiceImpl;
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
 * @since 2022-03-20
 */
@RestController
@RequestMapping("/api/financial_item")
public class FinancialItemsController {
    @Resource
    private FinancialItemsServiceImpl financialItemsService;


    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.FinancialItemPermission)
    @OperateRecord(operateName = "编辑理财产品",level = LevelCode.OPERATE_EDIT)
    @PostMapping("/edit_item")
    public Response editItem(HttpServletRequest request, @RequestBody FinancialItemVO financialItemVO) {
        Response res = financialItemsService.editFinancialItem(financialItemVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.FinancialItemPermission)
    @OperateRecord(operateName = "获取单个理财产品信息",level = LevelCode.OPERATE_READ)
    @PostMapping("/get_item")
    public Response getItem(HttpServletRequest request, @RequestBody QueryVO queryByIdVO) {
        Response res = financialItemsService.getFinancialItem(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.FinancialItemPermission)
    @OperateRecord(operateName = "分页查询理财产品信息",level = LevelCode.OPERATE_READ)
    @PostMapping("/get_page")
    public Response getPage(HttpServletRequest request, @RequestBody PageVO pageVO) {
        Response res = financialItemsService.getFinancialItemPage(pageVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.FinancialItemPermission)
    @OperateRecord(operateName = "删除理财产品",level = LevelCode.OPERATE_DELETE)
    @PostMapping("/delete_item")
    public Response deleteItem(HttpServletRequest request, @RequestBody QueryVO queryByIdVO) {
        Response res = financialItemsService.deleteFinancialItem(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }
}
