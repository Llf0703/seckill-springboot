package com.example.seckill_manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill_manager.common.Response;
import com.example.seckill_manager.controller.vo.ManagerUsersVO;
import com.example.seckill_manager.entity.ManagerUsers;
import com.example.seckill_manager.mapper.ManagerUsersMapper;
import com.example.seckill_manager.service.IManagerUsersService;
import com.example.seckill_manager.utils.JWTAuth;
import com.example.seckill_manager.utils.MD5;
import com.example.seckill_manager.utils.RedisUtils;
import com.example.seckill_manager.utils.Validator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-19
 */
@Service
public class ManagerUsersServiceImpl extends ServiceImpl<ManagerUsersMapper, ManagerUsers> implements IManagerUsersService {
    @Resource
    private ManagerUsersMapper managerUsersMapper;

    /*
     * @MethodName login
     * @author Wky1742095859
     * @Description 登录逻辑
     * @Date 2022/3/19 19:12
     * @Param [managerUsersVO, ip]
     * @Return com.example.seckill_manager.common.Response
     **/
    @Override
    public Response login(ManagerUsersVO managerUsersVO, String ip) {
        if (managerUsersVO.getAccount() == null || managerUsersVO.getPassword() == null)//判断是否为空
            return Response.authErr("账号或密码错误");
        if (ip == null) return Response.systemErr("登录失败,系统异常");//判断是否为空
        String VOAccount = managerUsersVO.getAccount();
        String VOPassword = managerUsersVO.getPassword();
        if (!Validator.isValidAccount(VOAccount) || !Validator.isValidPassword(VOPassword))
            return Response.authErr("账号或密码错误");//正则判断
        String MD5Password = MD5.MD5Password(VOAccount + VOPassword);
        if (Objects.equals(RedisUtils.get(VOAccount), MD5Password)) {
            String token = JWTAuth.releaseToken(managerUsersVO.getAccount());
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            String res = RedisUtils.set(VOAccount + "," + ip, token + "," + MD5Password, 3600);//缓存ip对应的token,及密码MD5(改密后需重新登录)
            String res2 = RedisUtils.set(ip + "_user", VOAccount, 3600);//缓存ip登录的用户(一个ip只能登录一个用户);
            if (!Objects.equals(res, "OK") || !Objects.equals(res2, "OK")) return Response.systemErr("登录失败,系统异常");
            return Response.success(data, "登录成功");
        }
        //缓存未查到数据,查询mysql
        QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("account", VOAccount);
        ManagerUsers managerUsers = managerUsersMapper.selectOne(queryWrapper);
        if (managerUsers == null || !Objects.equals(managerUsers.getPassword(), MD5Password))
            return Response.authErr("账号或密码错误");
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTAuth.releaseToken(managerUsersVO.getAccount());
        data.put("token", token);
        String res = RedisUtils.set(VOAccount + "," + ip, token + "," + MD5Password, 3600);
        String res2 = RedisUtils.set(managerUsers.getAccount(), managerUsers.getPassword());//缓存账号密码
        String res3 = RedisUtils.set(ip + "_user", VOAccount, 3600);
        if (!Objects.equals(res, "OK") || !Objects.equals(res2, "OK") || !Objects.equals(res3, "OK"))
            return Response.systemErr("登录失败,系统异常");
        return Response.success(data, "登录成功");
    }

    @Override
    public Response checkVersion(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.authErr("登录失效");
        if (ip == null) return Response.authErr("登录失效");
        String account = result.get("account").toString();
        String userInfoStr = RedisUtils.get(account + "," + ip);
        String MD5Password = RedisUtils.get(account);
        String loginUser = RedisUtils.get(ip + "_user");
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !loginUser.equals(account)) return Response.authErr("登录失效");
        if (userInfoStr == null) return Response.authErr("登录失效");
        String[] userInfo = userInfoStr.split(",");
        if (MD5Password == null) {
            QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at").eq("account", account);
            ManagerUsers managerUsers = managerUsersMapper.selectOne(queryWrapper);
            if (managerUsers == null) return Response.authErr("登录失效");
            RedisUtils.set(managerUsers.getAccount(), managerUsers.getPassword());
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            if (!Objects.equals(managerUsers.getPassword(), userInfo[1]) || !Objects.equals(token, userInfo[0]))
                return Response.authErr("登录失效");
            return Response.success("查询成功");
        }
        if (!Objects.equals(userInfo[0], token) || !Objects.equals(userInfo[1], MD5Password))
            return Response.authErr("登录失效");
        return Response.success("查询成功");
    }

    @Override
    public Response loginOut(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.success("退出成功");//token过期,直接返回退出成功
        String account = result.get("account").toString();
        if (RedisUtils.exist(account + "," + ip)) RedisUtils.del(account + "," + ip);
        if (RedisUtils.exist(ip + "_user")) RedisUtils.del(ip + "_user");
        return Response.success("退出成功");
    }
}
