package com.seckill.user.service;

import java.util.HashMap;

import com.seckill.user.entity.User;

public interface UserService {
    public HashMap<String, Object> register_service(User user);
    public HashMap<String, Object> login_service(User user);
    public HashMap<String, Object> check_version_service(String token);
}
