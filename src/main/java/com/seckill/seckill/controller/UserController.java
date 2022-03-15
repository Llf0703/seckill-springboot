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
        result.add_data(data);
        return result.getMap();
    }
}
