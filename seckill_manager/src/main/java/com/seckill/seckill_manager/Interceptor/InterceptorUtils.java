package com.seckill.seckill_manager.Interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.seckill_manager.Exception.InterceptorSystemException;
import com.seckill.seckill_manager.entity.ManagerUsers;
import com.seckill.seckill_manager.mapper.ManagerUsersMapper;
import com.seckill.seckill_manager.utils.JSONUtils;
import com.seckill.seckill_manager.utils.JWTAuth;
import com.seckill.seckill_manager.utils.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Component
public class InterceptorUtils {
    private static ManagerUsersMapper managerUsersMapper;
    @Resource
    private ManagerUsersMapper managerUsersMapper0;

    @PostConstruct
    private void init() {
        managerUsersMapper = this.managerUsersMapper0;
    }

    public static HashMap<String, Object> loginRequired(String token, String ip) {
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true) || ip == null) {
            res.put("status", false);
            return res;
        }
        String account = result.get("account").toString();
        String loginUser = RedisUtils.get(ip + "_user");
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !loginUser.equals(account)) {
            res.put("status", false);
            return res;
        }
        String loginInfoStr = RedisUtils.get(account + "," + ip);
        if (loginInfoStr == null) {
            res.put("status", false);
            return res;
        }
        String userInfoStr = RedisUtils.get(account);
        String[] loginInfo = loginInfoStr.split(",");
        if (userInfoStr == null) {
            QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at").eq("account", account);
            ManagerUsers managerUsers = managerUsersMapper.selectOne(queryWrapper);
            if (managerUsers == null) {
                res.put("status", false);
                return res;
            }
            String info = JSONUtils.toJSONStr(managerUsers);
            if (info == null) {
                throw new InterceptorSystemException("系统异常");
            }
            RedisUtils.set(managerUsers.getAccount(), info);
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            if (!Objects.equals(managerUsers.getPassword(), loginInfo[1]) || !Objects.equals(token, loginInfo[0])) {
                res.put("status", false);
                return res;
            }
            //return Objects.equals(managerUsers.getPassword(), userInfoStr[1]) && Objects.equals(token, userInfoStr[0]);
        }
        ManagerUsers userInfo = JSONUtils.toEntity(userInfoStr, ManagerUsers.class);
        if (userInfo == null) {
            throw new InterceptorSystemException("系统异常");
        }
        if (!Objects.equals(loginInfo[0], token) || !Objects.equals(userInfo.getPassword(), loginInfo[1])) {
            res.put("status", false);
            return res;
        }
        res.put("status", true);
        res.put("user", userInfo);
        return res;
        //return Objects.equals(userInfoStr[0], token) && Objects.equals(userInfoStr[1], userInfoStr);
    }
}
