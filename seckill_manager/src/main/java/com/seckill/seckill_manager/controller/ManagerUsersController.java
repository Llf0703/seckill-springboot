package com.seckill.seckill_manager.controller;


import com.seckill.seckill_manager.Interceptor.LevelCode;
import com.seckill.seckill_manager.Interceptor.PermissionType;
import com.seckill.seckill_manager.Interceptor.Type.LoginRequired;
import com.seckill.seckill_manager.Interceptor.Type.Permission;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
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


    @PostMapping("/auth/login")
    public Response login(HttpServletRequest request, @RequestBody ManagerUsersVO managerUsersVO) {
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.login(managerUsersVO, ip);
    }

    @GetMapping("/auth/check_version")
    public Response checkVersion(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.checkVersion(token, ip);
    }

    @PostMapping("/auth/log_out")
    public Response logOut(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.loginOut(token, ip);
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.AdminInfoPermission)
    @PostMapping("/admin/add_admin")
    public Response addAdmin(@RequestBody ManagerUsersVO managerUsersVO) {
        return managerUsersService.editAdmin(managerUsersVO);
    }

    @LoginRequired
    @Permission(level = LevelCode.EDIT, permission = PermissionType.AdminInfoPermission)
    @PostMapping("/admin/edit_admin")
    public Response editAdmin(@RequestBody ManagerUsersVO managerUsersVO) {
        return managerUsersService.editAdmin(managerUsersVO);
    }
    @LoginRequired
    @Permission(level = LevelCode.READ,permission = PermissionType.AdminInfoPermission)
    @PostMapping("/admin/get_admin")
    public Response getAdmin(@RequestBody QueryByIdVO queryByIdVO){
        return managerUsersService.getAdmin(queryByIdVO);
    }
}
