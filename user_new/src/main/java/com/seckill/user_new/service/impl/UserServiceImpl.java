package com.seckill.user_new.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.RegisterVO;
import com.seckill.user_new.controller.vo.UserVO;
import com.seckill.user_new.entity.RedisService.LoginCrypto;
import com.seckill.user_new.entity.RedisService.LoginUser;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.service.IUserService;
import com.seckill.user_new.utils.*;
import com.seckill.user_new.utils.crypto.AESUtil;
import com.seckill.user_new.utils.crypto.RSAUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
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
        HashMap<String, Object> cat = Captcha.getCircleCaptcha();
        data.put("captcha", cat.get("img"));
        data.put("encKey", encKey);//AES加密RSA公钥
        data.put("uid", MD5ID);
        res.setData(data);
        LoginCrypto loginCrypto = new LoginCrypto();//生成Redis对象
        loginCrypto.setAesKey(aesKey);//保存AES密钥
        loginCrypto.setRsaPrvKey(RSAPrvKey);//保存RSA私钥
        loginCrypto.setRsaPubKey(RSAPubKey);//保存RSA公钥
        loginCrypto.setTimeStamp(res.getTimeStamp());//保存生成时间
        loginCrypto.setCaptcha((String) cat.get("code"));//保存验证码
        //loginCrypto.setFp(managerUsersVO.getToken());//保存浏览器指纹
        loginCrypto.setIp(ip);//保存ip
        String result = RedisUtils.set("U:LoginCrypto:" + MD5ID, JSONUtils.toJSONStr(loginCrypto), 30);
        if (!Objects.equals(result, "OK")) return Response.systemErr("登录失败,系统异常");
        return res;
    }

    @Override
    public Response loginService(UserVO userVO, String ip) {
        if (userVO.getCaptcha() == null || Objects.equals(userVO.getCaptcha(), "")) return Response.paramsErr("请输入验证码");
        if (userVO.getPhone() == null || userVO.getPassword() == null) return Response.authErr("账号或密码错误");
        if (ip == null) return Response.systemErr("登录失败,系统异常");
        String loginCryptoStr = RedisUtils.get("U:LoginCrypto:" + userVO.getUid());
        if (loginCryptoStr == null) return Response.systemErr("请重新输入验证码");
        LoginCrypto loginCrypto = JSONUtils.toEntity(loginCryptoStr, LoginCrypto.class);
        if (loginCrypto == null) return Response.systemErr("登录失败,系统异常");
        if (!Objects.equals(loginCrypto.getCaptcha(), userVO.getCaptcha().toLowerCase()))
            return Response.authErr("验证码错误");
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
        if (!Validator.isValidPhone(VOPhone) || !Validator.isValidPassword(VOPassword) || VOFP.length() != 32) return Response.authErr("账号或密码错误");//正则判断
        String MD5Password = MD5.MD5Password(VOPhone + VOPassword);
        String userPassword = RedisUtils.hget("U:User:" + VOPhone,"password");//获取缓存的用户信息
        if (userPassword != null && !Objects.equals(userPassword, MD5Password)) {//缓存不为空且密码不相等
            return Response.authErr("账号或密码错误");
        }
        if (userPassword != null && Objects.equals(userPassword, MD5Password)) {//缓存不为空且密码相等
            String token = JWTAuth.releaseToken(VOPhone);
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            LoginUser loginUser = new LoginUser(VOPhone, MD5Password, VOFP, token, ip);
            String res = RedisUtils.set("U:LoginUser:" + VOPhone, JSONUtils.toJSONStr(loginUser), 3600);//缓存指纹对应的token,及密码MD5(改密后需重新登录)
            //String res2 = RedisUtils.set(ip + "_user", VOPhone, 3600);//缓存ip登录的用户(一个ip只能登录一个用户);
            //|| !Objects.equals(res2, "OK") 考虑到一个公网ip可能对应多个内网用户,记录ip弃用
            if (!Objects.equals(res, "OK")) return Response.systemErr("登录失败,系统异常");
            return Response.success(data, "登录成功");
        }
        //缓存未空,查询mysql
        User user;
        user = getUserByPhone(VOPhone);
        if (user!=null){
            Map<String,String> map=JSONUtils.toRedisHash(user);
            if (map!=null)RedisUtils.hset("U:User:"+user.getPhone(),map);
        }
        if (user == null || !Objects.equals(user.getPassword(), MD5Password))//未查到或密码不相等
            return Response.authErr("账号或密码错误");
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTAuth.releaseToken(VOPhone);
        data.put("token", token);
        LoginUser loginUser = new LoginUser(VOPhone, MD5Password, VOFP, token, ip);
        String res = RedisUtils.set("U:LoginUser:" + VOPhone, JSONUtils.toJSONStr(loginUser), 3600);
        //String res2 = RedisUtils.set("U:User:" + VOPhone, JSONUtils.toJSONStr(user), 25200);//缓存用户信息
        //String res3 = RedisUtils.set(ip + "_user", VOPhone, 3600);|| !Objects.equals(res2, "OK")
        if (!Objects.equals(res, "OK") ) return Response.systemErr("登录失败,系统异常");
        return Response.success(data, "登录成功");
    }

    @Override
    public Response checkVersion(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.authErr("登录失效");//token解析失败
        if (ip == null) return Response.authErr("登录失效");//无ip
        String account = result.get("account").toString();
        String loginUserStr = RedisUtils.get("U:LoginUser:" + account);
        //检查ip对应的登录账号及token账号是否一致
        if (loginUserStr == null) return Response.authErr("登录失效");
        LoginUser loginUser = JSONUtils.toEntity(loginUserStr, LoginUser.class);
        if (loginUser == null) return Response.systemErr("系统异常");
        if (!Objects.equals(loginUser.getIp(), ip) || !Objects.equals(token, loginUser.getToken()))
            return Response.authErr("登录失效");//当前ip无登录信息或token无效
        String userPassword = RedisUtils.hget("U:User:" + account,"password");//获取用户信息
        if (userPassword == null) {//未查到用户信息,查询数据库
            User user = null;
            user = getUserByPhone(account);
            if (user == null) return Response.authErr("登录失效");//账号不存在
            Map<String,String> map=JSONUtils.toRedisHash(user);
            if (map!=null)RedisUtils.hset("U:User:" + account,map);
            //RedisUtils.set("U:User:" + account, JSONUtils.toJSONStr(user), 25200);//缓存用户信息
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            if (!Objects.equals(user.getPassword(), loginUser.getMD5Password()))
                return Response.authErr("登录失效");//检查密码是否有更改,token是否一致
            return Response.success("查询成功");
        }
        if (!Objects.equals(userPassword, loginUser.getMD5Password()))
            return Response.authErr("登录失效");
        return Response.success("查询成功");
    }

    @Override
    public Response loginOut(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.success("退出成功");//token过期,直接返回退出成功
        String account = result.get("account").toString();
        //if (ip != null && RedisUtils.exist(account + "," + ip)) RedisUtils.del(account + "," + ip);
        if (ip != null && Boolean.TRUE.equals(RedisUtils.exists("U:LoginUser:" + account))) RedisUtils.del("U:LoginUser:" + account);
        return Response.success("退出成功");
    }

    @Override
    public Response register(RegisterVO registerVO, String ip) {
        if (registerVO.getCaptcha() == null || Objects.equals(registerVO.getCaptcha(), ""))
            return Response.paramsErr("请输入验证码");
        if (registerVO.getPhone() == null || registerVO.getPassword() == null
                || registerVO.getName() == null || registerVO.getIdCard() == null) return Response.authErr("有信息为空");
        if (ip == null) return Response.systemErr("注册失败,系统异常1");
        String loginCryptoStr = RedisUtils.get("U:LoginCrypto:" + registerVO.getUid());
        if (loginCryptoStr == null) return Response.systemErr("注册失败,请重新输入验证码");
        LoginCrypto loginCrypto = JSONUtils.toEntity(loginCryptoStr, LoginCrypto.class);
        if (loginCrypto == null) return Response.systemErr("注册失败,系统异常3");
        if (!Objects.equals(loginCrypto.getCaptcha(), registerVO.getCaptcha().toLowerCase()))
            return Response.authErr("验证码错误");
        byte[] VOPasswordBytes;
        byte[] VOPhoneBytes;
        byte[] VONameBytes;
        byte[] VOIdCardBytes;
        byte[] VOFPBytes;
        System.out.println(registerVO);
        try {
            VOPhoneBytes = RSAUtil.decryptByPrivateKey(Base64.decode(registerVO.getPhone()), loginCrypto.getRsaPrvKey());
            VOPasswordBytes = RSAUtil.decryptByPrivateKey(Base64.decode(registerVO.getPassword()), loginCrypto.getRsaPrvKey());
            VOFPBytes = RSAUtil.decryptByPrivateKey(Base64.decode(registerVO.getToken()), loginCrypto.getRsaPrvKey());
            VONameBytes = RSAUtil.decryptByPrivateKey(Base64.decode(registerVO.getName()), loginCrypto.getRsaPrvKey());
            VOIdCardBytes = RSAUtil.decryptByPrivateKey(Base64.decode(registerVO.getIdCard()), loginCrypto.getRsaPrvKey());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.authErr("信息错误");
        }
        String VOPassword = new String(VOPasswordBytes);
        String VOPhone = new String(VOPhoneBytes);
        String VOName = new String(VONameBytes);
        String VOIdCard = new String(VOIdCardBytes);
        String VOFP = new String(VOFPBytes);
        if (!Validator.isValidPhone(VOPhone) || !Validator.isValidPassword(VOPassword)
                || !Validator.isValidName(VOName) || !Validator.isValidIdCard(VOIdCard) || VOFP.length() != 32)
            return Response.authErr("信息不合法");
        if (getUserByPhone(VOPhone) != null)
            return Response.authErr("手机号已被注册");
        String MD5Password = MD5.MD5Password(VOPhone + VOPassword);
        LocalDateTime time = LocalDateTime.now();
        User user = new User();
        user.setCreatedAt(time);
        user.setUpdatedAt(time);
        user.setPhone(VOPhone);
        user.setPassword(MD5Password);
        user.setName(VOName);
        user.setIdCard(VOIdCard);
        user.setUserName(UserUtil.generateUserName());
        user.setBalance(BigDecimal.ZERO);
        user.setEmploymentStatus(0);
        user.setCreditStatus(0);
        try {
            LocalDateTime brithday = UserUtil.getAge(VOIdCard);
            user.setAge(brithday);
        } catch (ParseException e) {
            return Response.systemErr("注册失败,系统异常4");
        }
        if (!save(user)) return Response.systemErr("注册失败,系统异常5");
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTAuth.releaseToken(VOPhone);
        data.put("token", token);
        LoginUser loginUser = new LoginUser(VOPhone, MD5Password, VOFP, token, ip);
        String res = RedisUtils.set("U:LoginUser:" + VOPhone, JSONUtils.toJSONStr(loginUser), 3600);
        Map<String, String> map = JSONUtils.toRedisHash(user);
        if (map != null) RedisUtils.hset("U:User:" + VOPhone, map);//缓存用户信息
        //String res3 = RedisUtils.set(ip + "_user", VOPhone, 3600);|| !Objects.equals(res2, "OK")
        if (!Objects.equals(res, "OK")) return Response.systemErr("注册失败,系统异常6");
        return Response.success(data, "注册成功");
    }

    @Override
    public Response getUserInfo(HttpServletRequest request) {
        User user= (User) request.getAttribute("user");
        UserVO userVO=new UserVO();
        BeanUtil.copyProperties(user,userVO,true);
        return Response.success(userVO,"OK");
    }

    @Override
    public Response getBalance(HttpServletRequest request) {
        User user= (User) request.getAttribute("user");
        return Response.success(user.getBalance(),"OK");
    }

    private User getUserByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("binary phone", phone);
        return userMapper.selectOne(queryWrapper);
    }
}
