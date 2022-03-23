package com.seckill.seckill_manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.entity.ManagerUsers;
import com.seckill.seckill_manager.mapper.ManagerUsersMapper;
import com.seckill.seckill_manager.service.IManagerUsersService;
import com.seckill.seckill_manager.utils.*;
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
        String userInfoStr = RedisUtils.get(VOAccount);//获取缓存的用户信息
        String userPassword = null;
        ManagerUsers userInfo = null;
        if (userInfoStr != null) {//用户缓存不为空,进行str到实体类转换,并从实体类获取加密后的密码
            userInfo = JSONUtils.toEntity(userInfoStr, ManagerUsers.class);
            if (userInfo != null) {
                userPassword = userInfo.getPassword();
            }
        }
        if (userInfo != null && !Objects.equals(userPassword, MD5Password)) {//缓存不为空且密码不相等
            return Response.authErr("账号或密码错误");
        }
        if (userInfo != null && Objects.equals(userPassword, MD5Password)) {//缓存不为空且密码相等
            String token = JWTAuth.releaseToken(managerUsersVO.getAccount());
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            String res = RedisUtils.set(VOAccount + "," + ip, token + "," + MD5Password, 3600);//缓存ip对应的token,及密码MD5(改密后需重新登录)
            String res2 = RedisUtils.set(ip + "_user", VOAccount, 3600);//缓存ip登录的用户(一个ip只能登录一个用户);
            if (!Objects.equals(res, "OK") || !Objects.equals(res2, "OK")) return Response.systemErr("登录失败,系统异常");
            return Response.success(data, "登录成功");
        }
        //缓存未空,查询mysql
        ManagerUsers managerUsers = getManagerUserByAccount(VOAccount);
        if (managerUsers == null || !Objects.equals(managerUsers.getPassword(), MD5Password))//未查到或密码不相等
            return Response.authErr("账号或密码错误");
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTAuth.releaseToken(managerUsersVO.getAccount());
        data.put("token", token);
        String res = RedisUtils.set(VOAccount + "," + ip, token + "," + MD5Password, 3600);
        String res2 = RedisUtils.set(managerUsers.getAccount(), JSONUtils.toJSONStr(managerUsers));//缓存用户信息
        String res3 = RedisUtils.set(ip + "_user", VOAccount, 3600);
        if (!Objects.equals(res, "OK") || !Objects.equals(res2, "OK") || !Objects.equals(res3, "OK"))
            return Response.systemErr("登录失败,系统异常");
        return Response.success(data, "登录成功");
    }

    @Override
    public Response checkVersion(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.authErr("登录失效");//token解析失败
        if (ip == null) return Response.authErr("登录失效");//无ip
        String account = result.get("account").toString();
        String loginUser = RedisUtils.get(ip + "_user");
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !loginUser.equals(account)) return Response.authErr("登录失效");
        String loginInfoStr = RedisUtils.get(account + "," + ip);
        if (loginInfoStr == null) return Response.authErr("登录失效");//当前ip无登录信息
        String userInfoStr = RedisUtils.get(account);//获取用户信息
        String[] loginInfo = loginInfoStr.split(",");//提取token,登录时的密码
        ManagerUsers userInfo = JSONUtils.toEntity(userInfoStr, ManagerUsers.class);
        if (userInfo == null) {//未查到用户信息,查询数据库
            ManagerUsers managerUsers = getManagerUserByAccount(account);
            if (managerUsers == null) return Response.authErr("登录失效");//账号不存在
            RedisUtils.set(managerUsers.getAccount(), JSONUtils.toJSONStr(managerUsers));//缓存用户信息
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            if (!Objects.equals(managerUsers.getPassword(), loginInfo[1]) || !Objects.equals(token, loginInfo[0]))
                return Response.authErr("登录失效");//检查密码是否有更改,token是否一致
            return Response.success("查询成功");
        }
        if (!Objects.equals(loginInfo[0], token) || !Objects.equals(userInfo.getPassword(), loginInfo[1]))
            return Response.authErr("登录失效");
        return Response.success("查询成功");
    }

    @Override
    public Response loginOut(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.success("退出成功");//token过期,直接返回退出成功
        String account = result.get("account").toString();
        if (ip != null && RedisUtils.exist(account + "," + ip)) RedisUtils.del(account + "," + ip);
        if (ip != null && RedisUtils.exist(ip + "_user")) RedisUtils.del(ip + "_user");
        return Response.success("退出成功");
    }
    /*
     * @MethodName editAdmin
     * @author 滕
     * @Description 修改管理员信息
     * @Date 2022/3/23 21:59
     * @Param [managerUsersVO]
     * @Return com.seckill.seckill_manager.common.Response
    **/
    @Override
    public Response editAdmin(ManagerUsersVO managerUsersVO) {
        String MD5Password=MD5.MD5Password(managerUsersVO.getAccount()+managerUsersVO.getPassword());
        return Response.success("成功");
    }

    private ManagerUsers getManagerUserByAccount(String account) {
        QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("account", account);
        return managerUsersMapper.selectOne(queryWrapper);
    }

    private ManagerUsers getManagerUserById(ManagerUsersVO managerUsersVO) {
        if (managerUsersVO.getId() == null) return null;
        QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", managerUsersVO.getId());
        return managerUsersMapper.selectOne(queryWrapper);
    }
}
