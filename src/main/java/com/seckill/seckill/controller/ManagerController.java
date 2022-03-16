package com.seckill.seckill.controller;

import java.util.HashMap;

import javax.annotation.Resource;

import com.seckill.seckill.service.ManagerService;
import com.seckill.seckill.entity.Manager;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/manager/auth")
public class ManagerController {

    @Resource
    private ManagerService manager_service;

    @PostMapping("/register")
    public HashMap<String, Object> manager_register_controller(@RequestBody Manager user) {
        return manager_service.register_service(user);
    }

    @PostMapping("/login")
    public HashMap<String, Object> manager_login_controller(@RequestBody Manager user) {
        return manager_service.login_service(user);
    }

    @GetMapping("/check_version")
    public HashMap<String, Object> manager_check_version_controller(@RequestHeader("token") String token) {
        return manager_service.check_version_service(token);
    }
}
