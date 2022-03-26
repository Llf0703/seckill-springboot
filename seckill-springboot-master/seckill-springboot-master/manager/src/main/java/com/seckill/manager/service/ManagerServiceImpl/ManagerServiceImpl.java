package com.seckill.manager.service.ManagerServiceImpl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.manager.entity.Manager;
import com.seckill.manager.mapper.ManagerMapper;
import com.seckill.manager.utils.JWTUtil;
import com.seckill.manager.utils.MessageUitl;
import com.seckill.manager.utils.UserUtil;

import org.springframework.stereotype.Service;

import com.seckill.manager.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerMapper manager_mapper;
    
    @Override
    public HashMap<String, Object> register_service(Manager user) {
        Date date = new Date();
        user = user.toBuilder()
            .created_at(date)
            .updated_at(date)
            .build();
        user.setPassword(UserUtil.password_to_md5(user.getAccount(), user.getPassword()));
        manager_mapper.insert(user);
        HashMap<String, Object> data = new HashMap<String, Object>();
        String token = JWTUtil.createToken(user.getAccount());
        data.put("token", token);
        data.put("account", user.getAccount());
        MessageUitl result = new MessageUitl();
        result.success("ok");
        return result.getMap();
    }

    @Override
    public HashMap<String, Object> login_service(Manager user) {
        MessageUitl result = UserUtil.login_check(user);
        if (result.getMap().get("message")!="ok") return result.getMap();
        QueryWrapper<Manager> wrapper = new QueryWrapper<>();
        wrapper.eq("account", user.getAccount());
        long cnt = manager_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.auth_error("user not found");
            return result.getMap();
        }
        user.setPassword(UserUtil.password_to_md5(user.getAccount(), user.getPassword()));
        HashMap<String,Object> params = new HashMap<>();
        params.put("account", user.getAccount());
        params.put("password", user.getPassword());
        wrapper.allEq(params);
        cnt = manager_mapper.selectCount(wrapper);
        if (cnt == 0) {
            result.auth_error("wrong password");
            return result.getMap();
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        String token = JWTUtil.createToken(user.getAccount());
        data.put("token", token);
        data.put("account", user.getAccount());
        result.add_data(data);
        return result.getMap();
    }

    @Override
    public HashMap<String, Object> check_version_service(String token) {
        MessageUitl result = new MessageUitl();
        String account = JWTUtil.verifyToken(token);
        if (account != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("account", account);
            result.success("登录成功");
        }
        else result.auth_error("登录失败");
        return result.getMap();
    }
}
