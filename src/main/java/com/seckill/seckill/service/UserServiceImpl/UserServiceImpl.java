package com.seckill.seckill.service.UserServiceImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.seckill.entity.User;
import com.seckill.seckill.mapper.UserMapper;
import com.seckill.seckill.utils.JWTUtil;
import com.seckill.seckill.utils.MessageUitl;

import org.springframework.stereotype.Service;

import com.seckill.seckill.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper user_mapper;
    
    @Override
    public HashMap<String, Object> register_service(User user) {
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
            result.auth_error("user exist");
            return result.getMap();
        }
        try {
            user.get_age();
        } catch (ParseException e) {
            result.auth_error("invalid id card");
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

    @Override
    public HashMap<String, Object> login_service(User user) {
        MessageUitl result = user.login_check();
        if (result.getMap().get("message")!="ok") return result.getMap();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", user.getPhone());
        long cnt = user_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.auth_error("user not found");
            return result.getMap();
        }
        user.password_to_md5();
        HashMap<String,Object> params = new HashMap<>();
        params.put("phone", user.getPhone());
        params.put("password", user.getPassword());
        wrapper.allEq(params);
        cnt = user_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.auth_error("wrong password");
            return result.getMap();
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        String token = JWTUtil.createToken(user.getPhone());
        data.put("token", token);
        data.put("phone", user.getPhone());
        result.add_data(data);
        return result.getMap();
    }

    @Override
    public HashMap<String, Object> check_version_service(String token) {
        MessageUitl result = new MessageUitl();
        String phone = JWTUtil.verifyToken(token);
        if (phone != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("phone", phone);
            result.success("登录成功");
        }
        else result.auth_error("登录失败");
        return result.getMap();
    }
}
