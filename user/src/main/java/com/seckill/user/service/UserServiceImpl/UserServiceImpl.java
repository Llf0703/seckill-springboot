package com.seckill.user.service.UserServiceImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.user.entity.User;
import com.seckill.user.mapper.UserMapper;
import com.seckill.user.utils.JWTUtil;
import com.seckill.user.utils.MessageUitl;
import com.seckill.user.utils.UserUtil;

import org.springframework.stereotype.Service;

import com.seckill.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper user_mapper;
    
    @Override
    public HashMap<String, Object> register_service(User user) {
        MessageUitl result = UserUtil.register_check(user);
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
            Date brithday = UserUtil.get_age(user.getId_card());
            user.setAge(brithday);
        } catch (ParseException e) {
            result.auth_error("invalid id card");
            return result.getMap();
        }
        user.setUser_name(UserUtil.generate_user_name());
        user.setPassword(UserUtil.password_to_md5(user.getPhone(), user.getPassword()));
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
        MessageUitl result = UserUtil.login_check(user);
        if (result.getMap().get("message")!="ok") return result.getMap();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", user.getPhone());
        long cnt = user_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.auth_error("user not found");
            return result.getMap();
        }
        user.setPassword(UserUtil.password_to_md5(user.getPhone(), user.getPassword()));
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
