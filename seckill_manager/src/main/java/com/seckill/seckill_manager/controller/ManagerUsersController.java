package com.seckill.seckill_manager.controller;


import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.service.impl.ManagerUsersServiceImpl;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-19
 */
@RestController
@RequestMapping("/api/auth")
public class ManagerUsersController {
    @Resource
    private ManagerUsersServiceImpl managerUsersService;


    @PostMapping("/login")
    public Response login(HttpServletRequest request, @RequestBody ManagerUsersVO managerUsersVO) {
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.login(managerUsersVO, ip);
    }

    @GetMapping("/check_version")
    public Response checkVersion(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.checkVersion(token, ip);
    }

    @PostMapping("/log_out")
    public Response logOut(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return managerUsersService.loginOut(token, ip);
    }
}
