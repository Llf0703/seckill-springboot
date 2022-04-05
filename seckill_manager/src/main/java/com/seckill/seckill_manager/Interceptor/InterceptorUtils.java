package com.seckill.seckill_manager.Interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.seckill_manager.entity.ManagerUsers;
import com.seckill.seckill_manager.entity.RedisService.LoginAdmin;
import com.seckill.seckill_manager.mapper.ManagerUsersMapper;
import com.seckill.seckill_manager.utils.JSONUtils;
import com.seckill.seckill_manager.utils.JWTAuth;
import com.seckill.seckill_manager.utils.RedisUtils;
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
    private static ManagerUsersMapper managerUsersMapper;
    @Resource
    private ManagerUsersMapper managerUsersMapper0;

    @PostConstruct
    private void init() {
        managerUsersMapper = this.managerUsersMapper0;
    }

    public static boolean riskControlPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getRiskControlPermissions() >= level;
    }

    public static boolean seckillItemPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getSeckillItemsPermissions() >= level;
    }

    public static boolean seckilRecordPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getSeckillRecordPermissions() >= level;
    }

    public static boolean adminInfoPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getAdminInfoPermissions() >= level;
    }

    public static boolean guestInfoPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getGuestInfoPermissions() >= level;
    }

    public static boolean financialItemPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getFinancialItemsPermissions() >= level;
    }

    public static boolean rechargeRecordPermission(ManagerUsers user, int level) {
        if (user == null) return false;
        return user.getRechargeRecordPermissions() >= level;
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
        String loginUserStr = RedisUtils.get("M:LoginUser:" + account);
        LoginAdmin loginUser = null;
        if (loginUserStr != null)
            loginUser = JSONUtils.toEntity(loginUserStr, LoginAdmin.class);
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !Objects.equals(loginUser.getToken(), token) || !Objects.equals(loginUser.getIp(), ip)) {
            res.put("status", false);
            return res;
        }
        String managerUserStr = RedisUtils.get("M:ManagerUser:" + account);
        ManagerUsers managerUser = null;
        if (managerUserStr != null)
            managerUser = JSONUtils.toEntity(managerUserStr, ManagerUsers.class);
        if (managerUser == null) {//缓存中无账号信息,查询数据库
            QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at").eq("binary account", account);
            managerUser = managerUsersMapper.selectOne(queryWrapper);
            if (managerUser == null) {
                res.put("status", false);
                return res;
            }
            String info = JSONUtils.toJSONStr(managerUser);
            if (info != null) {
                RedisUtils.set("M:ManagerUser:" + account, info);
            }
            //检查密码是否更改
            if (!Objects.equals(managerUser.getPassword(), loginUser.getMD5Password())) {
                res.put("status", false);
                return res;
            }
            res.put("status", true);
            res.put("user", managerUser);
            return res;
        }
        if (!Objects.equals(managerUser.getPassword(), loginUser.getMD5Password())) {
            res.put("status", false);
            return res;
        }
        res.put("status", true);
        res.put("user", managerUser);
        return res;
    }
}
