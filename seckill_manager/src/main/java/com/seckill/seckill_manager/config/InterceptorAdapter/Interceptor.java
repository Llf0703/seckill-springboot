package com.seckill.seckill_manager.config.InterceptorAdapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill_manager.entity.ManagerUsers;
import com.example.seckill_manager.mapper.ManagerUsersMapper;
import com.example.seckill_manager.utils.JWTAuth;
import com.example.seckill_manager.utils.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Component
public class Interceptor {
    private static ManagerUsersMapper managerUsersMapper;
    @Resource
    private ManagerUsersMapper managerUsersMapper0;

    @PostConstruct
    private void init() {
        managerUsersMapper = this.managerUsersMapper0;
    }

    public static boolean loginRequired(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return false;
        if (ip == null) return false;
        String account = result.get("account").toString();
        String userInfoStr = RedisUtils.get(account + "," + ip);
        String MD5Password = RedisUtils.get(account);
        String loginUser = RedisUtils.get(ip + "_user");
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !loginUser.equals(account)) return false;
        if (userInfoStr == null) return false;
        String[] userInfo = userInfoStr.split(",");
        if (MD5Password == null) {
            QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at").eq("account", account);
            ManagerUsers managerUsers = managerUsersMapper.selectOne(queryWrapper);
            if (managerUsers == null) return false;
            RedisUtils.set(managerUsers.getAccount(), managerUsers.getPassword());
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            return Objects.equals(managerUsers.getPassword(), userInfo[1]) && Objects.equals(token, userInfo[0]);
        }
        return Objects.equals(userInfo[0], token) && Objects.equals(userInfo[1], MD5Password);
    }
}
