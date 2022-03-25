package com.seckill.user.controller;

import java.util.HashMap;

import javax.annotation.Resource;

import com.seckill.user.service.UserService;
import com.seckill.user.entity.User;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Resource
    private UserService user_service;

    @PostMapping("/register")
    public HashMap<String, Object> register_controller(@RequestBody User user) {
        return user_service.register_service(user);
    }

    @PostMapping("/login")
    public HashMap<String, Object> login_controller(@RequestBody User user) {
        return user_service.login_service(user);
    }

    @GetMapping("/check_version")
    public HashMap<String, Object> check_version_controller(@RequestHeader("token") String token) {
        return user_service.check_version_service(token);
    }
}
