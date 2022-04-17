package com.seckill.user_new.controller;

import com.seckill.user_new.Interceptor.Type.LoginRequired;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.RegisterVO;
import com.seckill.user_new.controller.vo.UserVO;
import com.seckill.user_new.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @PostMapping("/auth/login_check")
    public Response loginCheck(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        return userService.LoginCheck(ip);
    }

    @PostMapping("/auth/login")
    public Response login(HttpServletRequest request, @RequestBody UserVO userVO) {
        String ip = request.getHeader("X-real-ip");
        return userService.loginService(userVO, ip);
    }

    @PostMapping("/auth/register")
    public Response register(HttpServletRequest request, @RequestBody RegisterVO registerVO) {
        String ip = request.getHeader("X-real-ip");
        return userService.register(registerVO, ip);
    }


    @GetMapping("/auth/check_version")
    public Response checkVersion(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return userService.checkVersion(token, ip);
    }

    @PostMapping("/auth/log_out")
    public Response logOut(HttpServletRequest request) {
        String token = request.getHeader("token");
        String ip = request.getHeader("X-real-ip");
        return userService.loginOut(token, ip);
    }

    @LoginRequired
    @PostMapping("/user/get_user_info")
    public Response getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo(request);
    }

    @LoginRequired
    @PostMapping("/user/get_user_balance")
    public Response getUserBalance(HttpServletRequest request) {
        return userService.getBalance(request);
    }
}
