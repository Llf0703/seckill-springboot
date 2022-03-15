package com.seckill.seckill.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import com.seckill.seckill.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.seckill.entity.User;

import org.springframework.web.bind.annotation.*;

import com.seckill.seckill.utils.MessageUitl;
import com.seckill.seckill.utils.JWTUtil;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Resource
    private UserMapper user_mapper;

    @PostMapping("/register")
    public HashMap<String, Object> register_controller(@RequestBody User user) {
        MessageUitl result = user.register_check();
        if (result.getMap().get("message")!="ok") return result.getMap();
        Date date = new Date();
        user = user.toBuilder()
            .created_at(date)
            .updated_at(date)
            .employment_status(0)
            .credit_status(0)
            .build();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", user.getPhone());
        long cnt = user_mapper.selectCount(wrapper);
        if (cnt != 0) {
            result.init(300, "user exist", false);
            return result.getMap();
        }
        try {
            user.get_age();
        } catch (ParseException e) {
            result.init(300, "invalid id card", false);
            return result.getMap();
        }
        user.generate_user_name();
        user.password_to_md5();
        user_mapper.insert(user);
        HashMap<String, Object> data = new HashMap<String, Object>();
        String token = JWTUtil.createToken(user.getPhone());
        data.put("token", token);
        data.put("phone", user.getPhone());
        result.add_data(data);
        return result.getMap();
    }

    @PostMapping("/login")
    public HashMap<String, Object> login_controller(@RequestBody User user) {
        MessageUitl result = user.login_check();
        if (result.getMap().get("message")!="ok") return result.getMap();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", user.getPhone());
        long cnt = user_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.init(300, "user not found", false);
            return result.getMap();
        }
        user.password_to_md5();
        HashMap<String,Object> params = new HashMap<>();
        params.put("phone", user.getPhone());
        params.put("password", user.getPassword());
        wrapper.allEq(params);
        cnt = user_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.init(300, "wrong password", false);
            return result.getMap();
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        String token = JWTUtil.createToken(user.getPhone());
        data.put("token", token);
        data.put("phone", user.getPhone());
        result.add_data(data);
        return result.getMap();
    }

    @GetMapping("/check_version")
    public HashMap<String, Object> check_version_controller(@RequestHeader("token") String token) {
        MessageUitl result = new MessageUitl();
        String phone = JWTUtil.verifyToken(token);
        if (phone != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("phone", phone);
            result.init(200, "登录成功", true, data);
        }
        else result.init(300, "登录失败", false);
        return result.getMap();
    }
}
