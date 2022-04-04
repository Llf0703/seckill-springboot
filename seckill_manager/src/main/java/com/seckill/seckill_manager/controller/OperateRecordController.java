package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.service.impl.OperateRecordServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/api/operate_record")
public class OperateRecordController {
    @Resource
    private OperateRecordServiceImpl operateRecordService;

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.SeckillItemPermission)
    //@OperateRecord(operateName = "分页查询操作日志",level = LevelCode.OPERATE_READ)
    @PostMapping("/get_page")
    public Response getPage(HttpServletRequest request, @RequestBody PageVO pageVO) {
        Response res= operateRecordService.getOperateRecordPage(pageVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

}
