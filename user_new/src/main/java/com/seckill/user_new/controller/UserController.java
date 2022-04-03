package com.seckill.user_new.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.UserVO;
import com.seckill.user_new.service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/auth/login_check")
    public Response loginCheck(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        return userService.LoginCheck(ip);
    }

    @PostMapping("auth/login")
    public Response login(HttpServletRequest request, @RequestBody UserVO userVO) {
        String ip = request.getHeader("X-real-ip");
        return userService.loginService(userVO, ip);
    }
}
