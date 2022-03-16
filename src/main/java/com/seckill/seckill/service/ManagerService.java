package com.seckill.seckill.service;

import java.util.HashMap;

import com.seckill.seckill.entity.Manager;

public interface ManagerService {
    // just for test
    public HashMap<String, Object> register_service(Manager manager);

    public HashMap<String, Object> login_service(Manager manager);
    public HashMap<String, Object> check_version_service(String token);
}
