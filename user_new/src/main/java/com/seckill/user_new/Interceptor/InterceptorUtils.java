package com.seckill.user_new.Interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.user_new.entity.RedisService.LoginUser;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.JWTAuth;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName InterceptorUtils
 * @description: 权限处理类
 * @date 2022/3/25 2:28
 */
@Component
public class InterceptorUtils {
    private static UserMapper userMapper;
    @Resource
    private UserMapper userMapper0;

    @PostConstruct
    private void init() {
        userMapper = this.userMapper0;
    }

    /*
     * @MethodName loginRequired
     * @author Wky1742095859
     * @Description 鉴别是否登录
     * @Date 2022/3/23 2:49
     * @Param [token, ip]
     * @Return java.util.HashMap<java.lang.String,java.lang.Object>
     **/
    public static HashMap<String, Object> loginRequired(String token, String ip) {
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true) || ip == null) {
            res.put("status", false);
            return res;
        }
        String account = result.get("account").toString();
        String loginUserStr = RedisUtils.get("U:LoginUser:" + account);
        LoginUser loginUser = null;
        if (loginUserStr != null)
            loginUser = JSONUtils.toEntity(loginUserStr, LoginUser.class);
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !Objects.equals(loginUser.getToken(), token) || !Objects.equals(loginUser.getIp(), ip)) {
            res.put("status", false);
            return res;
        }
        String userStr = RedisUtils.get("U:User:" + account);
        User user = null;
        if (userStr != null)
            user = JSONUtils.toEntity(userStr, User.class);
        if (user == null) {//缓存中无账号信息,查询数据库
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at").eq("binary phone", account);
            user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                res.put("status", false);
                return res;
            }
            String info = JSONUtils.toJSONStr(user);
            if (info != null) {
                RedisUtils.set("U:User:" + account, info);
            }
            //检查密码是否更改
            if (!Objects.equals(user.getPassword(), loginUser.getMD5Password())) {
                res.put("status", false);
                return res;
            }
            res.put("status", true);
            res.put("user", user);
            return res;
        }
        if (!Objects.equals(user.getPassword(), loginUser.getMD5Password())) {
            res.put("status", false);
            return res;
        }
        res.put("status", true);
        res.put("user", user);
        return res;
    }
}
