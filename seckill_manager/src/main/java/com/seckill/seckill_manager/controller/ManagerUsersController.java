package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.OperateRecord;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
import com.seckill.seckill_manager.service.impl.ManagerUsersServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-19
 */
@RestController
@RequestMapping("/api")
public class ManagerUsersController {
    @Resource
    private ManagerUsersServiceImpl managerUsersService;

    @PostMapping("/auth/login_check")
    public Response loginCheck(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.LoginCheck(ip);

    }

    @OperateRecord(operateName = "登录")
    @PostMapping("/auth/login")
    public Response login(HttpServletRequest request, @RequestBody ManagerUsersVO managerUsersVO) {
        String ip = request.getHeader("X-real-ip");
        Response res = managerUsersService.login(managerUsersVO, ip, request);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @GetMapping("/auth/check_version")
    public Response checkVersion(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        Response res = managerUsersService.checkVersion(token, ip);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @PostMapping("/auth/log_out")
    public Response logOut(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        Response res = managerUsersService.loginOut(token, ip);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @PostMapping("/auth/get_user_info")
    public Response getUserInfo(HttpServletRequest request) {
        return managerUsersService.getUserInfo(request);
    }

    @LoginRequired
    @PostMapping("/auth/reset_password/valid_password")
    public Response validPassword(HttpServletRequest request, @RequestBody ManagerUsersVO managerUsersVO) {
        return managerUsersService.validPassword(request, managerUsersVO);
    }


    @PostMapping("/auth/reset_password/reset/{uid}")
    public Response resetPassword(HttpServletRequest request, @PathVariable("uid") String uid, @RequestBody ManagerUsersVO managerUsersVO) {
        return managerUsersService.resetPassword(request, uid, managerUsersVO);
    }


    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.AdminInfoPermission)
    @OperateRecord(operateName = "编辑管理员")
    @PostMapping("/admin/edit_admin")
    public Response editAdmin(HttpServletRequest request, @RequestBody ManagerUsersVO managerUsersVO) {
        Response res = managerUsersService.editAdmin(managerUsersVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.AdminInfoPermission)
    @OperateRecord(operateName = "获取单个管理员信息")
    @PostMapping("/admin/get_admin")
    public Response getAdmin(HttpServletRequest request, @RequestBody QueryByIdVO queryByIdVO) {
        Response res = managerUsersService.getAdmin(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.READ, permission = PermissionType.AdminInfoPermission)
    @OperateRecord(operateName = "分页查询管理员信息")
    @PostMapping("/admin/get_page")
    public Response getPage(HttpServletRequest request, @RequestBody PageVO pageVO) {
        Response res = managerUsersService.getAdminPage(pageVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.AdminInfoPermission)
    @OperateRecord(operateName = "删除管理员")
    @PostMapping("/admin/delete_admin")
    public Response deleteAdmin(HttpServletRequest request, @RequestBody QueryByIdVO queryByIdVO) {
        Response res = managerUsersService.deleteAdmin(queryByIdVO);
        if (res.getStatus()) {
            request.setAttribute("operateId", res.getId());
        }
        return res;
    }


}
