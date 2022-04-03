package com.seckill.user_new.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Resource;

import cn.hutool.core.codec.Base64;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.UserVO;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.entity.RedisService.LoginCrypto;
import com.seckill.user_new.entity.RedisService.LoginUser;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.service.UserService;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.JWTAuth;
import com.seckill.user_new.utils.MD5;
import com.seckill.user_new.utils.RedisUtils;
import com.seckill.user_new.utils.Snowflake;
import com.seckill.user_new.utils.Validator;
import com.seckill.user_new.utils.crypto.AESUtil;
import com.seckill.user_new.utils.crypto.RSAUtil;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Response LoginCheck(String ip) {
        String RSAPubKey = RSAUtil.getPublicKey();//生成RSA公钥
        Response res = Response.success("OK");
        String id = Snowflake.nextID();
        String MD5ID = MD5.toMD5String(id);
        String aesKey = MD5ID.substring(16);//生成AES密钥
        String RSAPrvKey = RSAUtil.getPrivateKey();//生成RSA私钥
        HashMap<String, Object> data = new HashMap<>();
        String encKey;
        try {
            encKey = AESUtil.encrypt(RSAPubKey, aesKey);
        } catch (Exception e) {
            return Response.systemErr("登录失败,系统异常");
        }
        if (encKey == null) return Response.systemErr("登录失败,系统异常");
        encKey = Base64.encode(encKey.getBytes(StandardCharsets.UTF_8));
        data.put("encKey", encKey);//AES加密RSA公钥
        data.put("uid", MD5ID);
        res.setData(data);
        LoginCrypto loginCrypto = new LoginCrypto();//生成Redis对象
        loginCrypto.setAesKey(aesKey);//保存AES密钥
        loginCrypto.setRsaPrvKey(RSAPrvKey);//保存RSA私钥
        loginCrypto.setRsaPubKey(RSAPubKey);//保存RSA公钥
        loginCrypto.setTimeStamp(res.getTimeStamp());//保存生成时间
        //loginCrypto.setFp(managerUsersVO.getToken());//保存浏览器指纹
        loginCrypto.setIp(ip);//保存ip
        String result = RedisUtils.set("M:LoginCrypto:" + MD5ID, JSONUtils.toJSONStr(loginCrypto), 30);
        if (!Objects.equals(result, "OK")) return Response.systemErr("登录失败,系统异常");
        return res;
    }

    @Override
    public Response loginService(UserVO userVO, String ip) {
        if (userVO.getPhone() == null || userVO.getPassword() == null)
            return Response.authErr("账号或密码错误");
        if (ip == null) return Response.systemErr("登录失败,系统异常");
        String loginCryptoStr = RedisUtils.get("M:LoginCrypto:" + userVO.getUid());
        if (loginCryptoStr == null) return Response.systemErr("登录失败,系统异常");
        LoginCrypto loginCrypto = JSONUtils.toEntity(loginCryptoStr, LoginCrypto.class);
        if (loginCrypto == null) return Response.systemErr("登录失败,系统异常");
        byte[] VOPasswordBytes;
        byte[] VOPhoneBytes;
        byte[] VOFPBytes;
        try {
            VOPhoneBytes = RSAUtil.decryptByPrivateKey(Base64.decode(userVO.getPhone()), loginCrypto.getRsaPrvKey());
            VOPasswordBytes = RSAUtil.decryptByPrivateKey(Base64.decode(userVO.getPassword()), loginCrypto.getRsaPrvKey());
            VOFPBytes = RSAUtil.decryptByPrivateKey(Base64.decode(userVO.getToken()), loginCrypto.getRsaPrvKey());
        } catch (Exception e) {
            return Response.authErr("账号或密码错误");
        }
        String VOPassword = new String(VOPasswordBytes);
        String VOPhone = new String(VOPhoneBytes);
        String VOFP = new String(VOFPBytes);
        if (!Validator.isValidPhone(VOPhone) || !Validator.isValidPassword(VOPassword) || VOFP.length() != 32)
            return Response.authErr("账号或密码错误");//正则判断
        String MD5Password = MD5.MD5Password(VOPhone + VOPassword);
        String userStr = RedisUtils.get(VOPhone);//获取缓存的用户信息
        String userPassword = null;
        User user;
        if (userStr != null) {//用户缓存不为空,进行str到实体类转换,并从实体类获取加密后的密码
            user = JSONUtils.toEntity(userStr, User.class);
            if (user != null) {
                userPassword = user.getPassword();
            }
        }
        if (userPassword != null && !Objects.equals(userPassword, MD5Password)) {//缓存不为空且密码不相等
            return Response.authErr("账号或密码错误");
        }
        if (userPassword != null && Objects.equals(userPassword, MD5Password)) {//缓存不为空且密码相等
            String token = JWTAuth.releaseToken(VOPhone);
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            LoginUser loginUser = new LoginUser(VOPhone, MD5Password, VOFP, token, ip);
            String res = RedisUtils.set("M:LoginUser:" + VOPhone, JSONUtils.toJSONStr(loginUser), 3600);//缓存指纹对应的token,及密码MD5(改密后需重新登录)
            //String res2 = RedisUtils.set(ip + "_user", VOPhone, 3600);//缓存ip登录的用户(一个ip只能登录一个用户);
            //|| !Objects.equals(res2, "OK") 考虑到一个公网ip可能对应多个内网用户,记录ip弃用
            if (!Objects.equals(res, "OK")) return Response.systemErr("登录失败,系统异常");
            return Response.success(data, "登录成功");
        }
        //缓存未空,查询mysql
        user = getUserByPhone(VOPhone);
        if (user == null || !Objects.equals(user.getPassword(), MD5Password))//未查到或密码不相等
            return Response.authErr("账号或密码错误");
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTAuth.releaseToken(userVO.getPhone());
        data.put("token", token);
        LoginUser loginUser = new LoginUser(VOPhone, MD5Password, VOFP, token, ip);
        String res = RedisUtils.set("M:LoginUser:" + VOPhone, JSONUtils.toJSONStr(loginUser), 3600);
        String res2 = RedisUtils.set("M:ManagerUser:" + VOPhone, JSONUtils.toJSONStr(user), 25200);//缓存用户信息
        //String res3 = RedisUtils.set(ip + "_user", VOPhone, 3600);
        if (!Objects.equals(res, "OK") || !Objects.equals(res2, "OK")) return Response.systemErr("登录失败,系统异常");
        return Response.success(data, "登录成功");
    }

    private User getUserByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("binary phone", phone);
        return userMapper.selectOne(queryWrapper);
    }
}
