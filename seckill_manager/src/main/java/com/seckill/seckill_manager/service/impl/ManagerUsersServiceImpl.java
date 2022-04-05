package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.dto.ManagerUserDTO;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.entity.ManagerUsers;
import com.seckill.seckill_manager.entity.RedisService.LoginAdmin;
import com.seckill.seckill_manager.entity.RedisService.LoginCrypto;
import com.seckill.seckill_manager.mapper.ManagerUsersMapper;
import com.seckill.seckill_manager.service.IManagerUsersService;
import com.seckill.seckill_manager.utils.*;
import com.seckill.seckill_manager.utils.crypto.AESUtil;
import com.seckill.seckill_manager.utils.crypto.RSAUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
     * @MethodName LoginCheck
     * @author Wky1742095859
     * @Description 登录step1 生成加密参数
     * @Date 2022/4/1 21:23
     * @Param [managerUsersVO, ip]
     * @Return com.seckill.seckill_manager.common.Response
     **/
    @Override
    public Response LoginCheck(String ip) {
        //if (managerUsersVO.getAccount() == null) return Response.paramsErr("参数错误");
        String RSAPubKey = RSAUtil.getPublicKey();//生成RSA公钥
        Response res = Response.success("OK", 0);
        String id = Snowflake.nextID();
        String MD5ID = MD5.toMD5String(id);
        String aesKey = MD5ID.substring(16);//生成AES密钥
        String RSAPrvKey = RSAUtil.getPrivateKey();//生成RSA私钥
        HashMap<String, Object> data = new HashMap<>();
        String encKey;
        try {
            encKey = AESUtil.encrypt(RSAPubKey, aesKey);
        } catch (Exception e) {
            return Response.systemErr("系统异常");
        }
        if (encKey == null) return Response.systemErr("系统异常");
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

        String result = RedisUtils.set("M:LoginCrypto:" + MD5ID, JSONUtils.toJSONStr(loginCrypto), 90);
        if (!Objects.equals(result, "OK")) return Response.systemErr("系统异常");
        return res;
    }

    /*
     * @MethodName login
     * @author Wky1742095859
     * @Description 登录逻辑
     * @Date 2022/3/19 19:12
     * @Param [managerUsersVO, ip]
     * @Return com.example.seckill_manager.common.Response
     **/
    @Override
    public Response login(ManagerUsersVO managerUsersVO, String ip, HttpServletRequest request) {
        /*经修改后不再限制ip只能登录一个号,前端传输的账号密码也进行了加密处理*/
        /*account:rsa加密账号 password:rsa加密密码 token:加密后的浏览器指纹,uid:即loginCheck中uid*/
        if (managerUsersVO.getCaptcha() == null) return Response.paramsErr("请输入验证码");
        if (managerUsersVO.getAccount() == null || managerUsersVO.getPassword() == null || managerUsersVO.getToken() == null || managerUsersVO.getUid() == null)//判断是否为空
            return Response.authErr("账号或密码错误");
        if (ip == null) return Response.systemErr("登录失败,系统异常");//判断是否为空
        String loginCryptoStr = RedisUtils.get("M:LoginCrypto:" + managerUsersVO.getUid());
        if (loginCryptoStr == null) return Response.systemErr("请重新输入验证码");
        LoginCrypto loginCrypto = JSONUtils.toEntity(loginCryptoStr, LoginCrypto.class);
        if (loginCrypto == null) return Response.systemErr("登录失败,系统异常");
        if (!Objects.equals(loginCrypto.getCaptcha(), managerUsersVO.getCaptcha().toLowerCase()))
            return Response.authErr("验证码错误");
        byte[] VOPasswordBytes;
        byte[] VOAccountBytes;
        byte[] VOFPBytes;
        try {
            VOAccountBytes = RSAUtil.decryptByPrivateKey(Base64.decode(managerUsersVO.getAccount()), loginCrypto.getRsaPrvKey());
            VOPasswordBytes = RSAUtil.decryptByPrivateKey(Base64.decode(managerUsersVO.getPassword()), loginCrypto.getRsaPrvKey());
            VOFPBytes = RSAUtil.decryptByPrivateKey(Base64.decode(managerUsersVO.getToken()), loginCrypto.getRsaPrvKey());
        } catch (Exception e) {
            return Response.authErr("账号或密码错误");
        }
        String VOPassword = new String(VOPasswordBytes);
        String VOAccount = new String(VOAccountBytes);
        String VOFP = new String(VOFPBytes);
        if (!Validator.isValidAccount(VOAccount) || !Validator.isValidPassword(VOPassword) || VOFP.length() != 32)
            return Response.authErr("账号或密码错误");//正则判断
        String MD5Password = MD5.MD5Password(VOAccount + VOPassword);
        String ManagerUserStr = RedisUtils.get("M:ManagerUser:" + VOAccount);//获取缓存的用户信息
        String managerUserPassword = null;
        ManagerUsers managerUser = new ManagerUsers();
        if (ManagerUserStr != null) {//用户缓存不为空,进行str到实体类转换,并从实体类获取加密后的密码
            managerUser = JSONUtils.toEntity(ManagerUserStr, ManagerUsers.class);
            if (managerUser != null) {
                managerUserPassword = managerUser.getPassword();
            }
        }
        if (managerUserPassword != null && !Objects.equals(managerUserPassword, MD5Password)) {//缓存不为空且密码不相等
            return Response.authErr("账号或密码错误");
        }
        if (managerUserPassword != null && Objects.equals(managerUserPassword, MD5Password)) {//缓存不为空且密码相等
            String token = JWTAuth.releaseToken(VOAccount);
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            LoginAdmin loginAdmin = new LoginAdmin(VOAccount, MD5Password, VOFP, token, ip);
            String res = RedisUtils.set("M:LoginUser:" + VOAccount, JSONUtils.toJSONStr(loginAdmin), 3600);//缓存指纹对应的token,及密码MD5(改密后需重新登录)
            //String res2 = RedisUtils.set(ip + "_user", VOAccount, 3600);//缓存ip登录的用户(一个ip只能登录一个用户);
            //|| !Objects.equals(res2, "OK") 考虑到一个公网ip可能对应多个内网用户,记录ip弃用
            if (!Objects.equals(res, "OK")) return Response.systemErr("登录失败,系统异常");
            request.setAttribute("user", managerUser);
            return Response.success(data, "登录成功", managerUser.getId());
        }
        //缓存未空,查询mysql
        System.out.println("查询sql");
        ManagerUsers managerUsers = getManagerUserByAccount(VOAccount);
        if (managerUsers == null || !Objects.equals(managerUsers.getPassword(), MD5Password))//未查到或密码不相等
            return Response.authErr("账号或密码错误");
        HashMap<String, Object> data = new HashMap<>();
        String token = JWTAuth.releaseToken(managerUsersVO.getAccount());
        data.put("token", token);
        LoginAdmin loginAdmin = new LoginAdmin(VOAccount, MD5Password, VOFP, token, ip);
        String res = RedisUtils.set("M:LoginUser:" + VOAccount, JSONUtils.toJSONStr(loginAdmin), 3600);
        String res2 = RedisUtils.set("M:ManagerUser:" + VOAccount, JSONUtils.toJSONStr(managerUsers), 25200);//缓存用户信息
        //String res3 = RedisUtils.set(ip + "_user", VOAccount, 3600);
        if (!Objects.equals(res, "OK") || !Objects.equals(res2, "OK")) return Response.systemErr("登录失败,系统异常");
        request.setAttribute("user", managerUser);
        return Response.success(data, "登录成功", managerUsers.getId());
    }

    @Override
    public Response checkVersion(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.authErr("登录失效");//token解析失败
        if (ip == null) return Response.authErr("登录失效");//无ip
        String account = result.get("account").toString();
        String loginUserStr = RedisUtils.get("M:LoginUser:" + account);
        //检查ip对应的登录账号及token账号是否一致
        if (loginUserStr == null) return Response.authErr("登录失效");
        LoginAdmin loginUser = JSONUtils.toEntity(loginUserStr, LoginAdmin.class);
        if (loginUser == null) return Response.systemErr("系统异常");
        if (!Objects.equals(loginUser.getIp(), ip) || !Objects.equals(token, loginUser.getToken()))
            return Response.authErr("登录失效");//当前ip无登录信息或token无效
        String managerUserStr = RedisUtils.get("M:ManagerUser:" + account);//获取用户信息
        ManagerUsers managerUser = null;
        if (managerUserStr != null) managerUser = JSONUtils.toEntity(managerUserStr, ManagerUsers.class);
        if (managerUser == null) {//未查到用户信息,查询数据库
            managerUser = getManagerUserByAccount(account);
            if (managerUser == null) return Response.authErr("登录失效");//账号不存在
            RedisUtils.set("M:ManagerUser:" + account, JSONUtils.toJSONStr(managerUser), 25200);//缓存用户信息
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            if (!Objects.equals(managerUser.getPassword(), loginUser.getMD5Password()))
                return Response.authErr("登录失效");//检查密码是否有更改,token是否一致
            return Response.success("查询成功", 0);
        }
        if (!Objects.equals(managerUser.getPassword(), loginUser.getMD5Password()))
            return Response.authErr("登录失效");
        return Response.success("查询成功", 0);
    }

    @Override
    public Response loginOut(String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.success("退出成功", 0);//token过期,直接返回退出成功
        String account = result.get("account").toString();
        //if (ip != null && RedisUtils.exist(account + "," + ip)) RedisUtils.del(account + "," + ip);
        if (ip != null && RedisUtils.exist("M:LoginUser:" + account)) RedisUtils.del("M:LoginUser:" + account);
        return Response.success("退出成功", 0);
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
        if (managerUsersVO.getCaptcha() == null) return Response.paramsErr("请输入验证码");
        if (managerUsersVO.getToken() == null || managerUsersVO.getUid() == null)//判断是否为空
            return Response.systemErr("保存失败,数据异常");
        String loginCryptoStr = RedisUtils.get("M:LoginCrypto:" + managerUsersVO.getUid());
        if (loginCryptoStr == null) return Response.systemErr("请重新输入验证码");
        LoginCrypto loginCrypto = JSONUtils.toEntity(loginCryptoStr, LoginCrypto.class);
        if (loginCrypto == null) return Response.systemErr("保存失败,系统异常");
        if (!Objects.equals(loginCrypto.getCaptcha(), managerUsersVO.getCaptcha().toLowerCase()))
            return Response.authErr("验证码错误");
        if (!Validator.isValidZeroOrOneOrTwo(managerUsersVO.getSeckillItemsPermissions()))
            return Response.paramsErr("秒杀活动管理权限错误");
        if (!Validator.isValidZeroOrOne(managerUsersVO.getRechargeRecordPermissions()))
            return Response.paramsErr("充值记录管理权限错误");
        if (!Validator.isValidZeroOrOne(managerUsersVO.getSeckillRecordPermissions()))
            return Response.paramsErr("秒杀记录管理权限错误");
        if (!Validator.isValidZeroOrOneOrTwo(managerUsersVO.getAdminInfoPermissions()))
            return Response.paramsErr("管理员信息管理权限错误");
        if (!Validator.isValidZeroOrOneOrTwo(managerUsersVO.getFinancialItemsPermissions()))
            return Response.paramsErr("理财产品管理权限错误");
        if (!Validator.isValidZeroOrOneOrTwo(managerUsersVO.getRiskControlPermissions()))
            return Response.paramsErr("风险引擎管理权限错误");
        if (!Validator.isValidZeroOrOneOrTwo(managerUsersVO.getGuestInfoPermissions()))
            return Response.paramsErr("客户信息管理权限错误");
        byte[] VOPasswordBytes = null;
        byte[] VOAccountBytes = null;
        byte[] VOFPBytes;
        try {
            VOFPBytes = RSAUtil.decryptByPrivateKey(Base64.decode(managerUsersVO.getToken()), loginCrypto.getRsaPrvKey());
        } catch (Exception e) {
            return Response.paramsErr("保存失败,数据异常");
        }
        if (managerUsersVO.getPassword() != null) {//密码不为空,对密码进行解密
            try {
                VOPasswordBytes = RSAUtil.decryptByPrivateKey(Base64.decode(managerUsersVO.getPassword()), loginCrypto.getRsaPrvKey());
            } catch (Exception e) {
                return Response.paramsErr("保存失败,数据异常");
            }
        }
        if (managerUsersVO.getId() == null) {//id为空,对账号进行解密
            try {
                VOAccountBytes = RSAUtil.decryptByPrivateKey(Base64.decode(managerUsersVO.getAccount()), loginCrypto.getRsaPrvKey());
            } catch (Exception e) {
                return Response.paramsErr("保存失败,数据异常");
            }
        }
        String VOAccount = null;
        String VOPassword = null;
        if (VOAccountBytes != null) VOAccount = new String(VOAccountBytes);
        if (VOPasswordBytes != null) VOPassword = new String(VOPasswordBytes);
        String VOFP = new String(VOFPBytes);
        if (VOFP.length() != 32) return Response.paramsErr("保存失败,数据异常");
        if (managerUsersVO.getId() == null) {
            if (!Validator.isValidAccount(VOAccount)) return Response.paramsErr("账号格式不合规");
        }
        if (managerUsersVO.getPassword() != null) {
            if (!Validator.isValidPassword(VOPassword)) return Response.paramsErr("密码格式不合规");
        }
        //给权限编辑管理员信息
        //这个是修改或添加管理员信息的接口
        ManagerUsers managerUsers = new ManagerUsers();
        //根据id是否为空来判断当前操作是修改还是新增管理员
        if (managerUsersVO.getId() == null) {  //id为空，新增
            //新增管理员信息
            ManagerUsers query = getManagerUserByAccount(VOAccount);//禁止重复的账号名
            if (query != null) return Response.paramsErr("此账号已被注册");
            BeanUtil.copyProperties(managerUsersVO, managerUsers, true);
            LocalDateTime localDateTime = LocalDateTime.now();
            managerUsers.setCreatedAt(localDateTime);
            managerUsers.setUpdatedAt(localDateTime);
            managerUsers.setAccount(VOAccount);
            managerUsers.setPassword(MD5.MD5Password(VOAccount + VOPassword));
            boolean res = save(managerUsers);
            if (!res) return Response.dataErr("保存失败,数据库异常");
            String res1 = RedisUtils.set("M:ManagerUser:" + managerUsers.getAccount(), JSONUtils.toJSONStr(managerUsers), 25200);
            if (!Objects.equals(res1, "OK")) return Response.success("新增成功", managerUsers.getId());
            return Response.success("新增成功", managerUsers.getId());
        }
        //id不为空修改
        managerUsers = getManagerUserById(managerUsersVO.getId());//id不为空的话需要事先查一次数据库：
        // 如果不存在id为xxx的数据，则返回管理员不存在
        if (managerUsers == null) return Response.paramsErr("未找到该管理员");
        //id存在
        VOAccount = managerUsers.getAccount();//防止账号被修改
        LocalDateTime localDateTime = LocalDateTime.now();
        BeanUtil.copyProperties(managerUsersVO, managerUsers, true);//更改字段(VO源，managerUsers靶，忽略不需要的字段）
        managerUsers.setUpdatedAt(localDateTime);
        managerUsers.setAccount(VOAccount);
        if (managerUsersVO.getPassword() != null)
            managerUsers.setPassword(MD5.MD5Password(managerUsers.getAccount() + VOPassword));
        if (!updateById(managerUsers)) return Response.dataErr("保存失败,数据库异常");
        String res1 = RedisUtils.set("M:ManagerUser:" + managerUsers.getAccount(), JSONUtils.toJSONStr(managerUsers), 25200);
        if (!Objects.equals(res1, "OK")) {
            RedisUtils.del("M:ManagerUser:" + managerUsers.getAccount());
            return Response.success("保存成功,但可能需要等待一段时间才能应用变更", managerUsers.getId());
        }
        return Response.success("保存成功", managerUsers.getId());
    }

    @Override
    public Response getAdmin(QueryVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        ManagerUsers managerUsers = getManagerUserById(queryByIdVO.getId());
        if (managerUsers == null) return Response.dataNotFoundErr("未查询到相关数据");
        return Response.success(ManagerUserDTO.toManagerUserPostFormDTO(managerUsers), "获取成功", managerUsers.getId());
    }

    @Override
    public Response getAdminPage(PageVO pageVO) {
        if (!Validator.isValidZeroOrOne(pageVO.getOrder())) return Response.paramsErr("排序异常");
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        if (pageVO.getKeyWord() != null && !Validator.isValidProductName(pageVO.getKeyWord()))
            return Response.paramsErr("关键词异常");
        Page<ManagerUsers> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
        if (pageVO.getKeyWord() != null) queryWrapper.like("account", pageVO.getKeyWord());
        queryWrapper.isNull("deleted_at");
        if (pageVO.getOrder() == 1) {
            queryWrapper.orderByDesc("id");
        }
        managerUsersMapper.selectPage(page, queryWrapper);
        List<ManagerUsers> itemsList = page.getRecords();
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", ManagerUserDTO.toManagerUserTableDTO(itemsList));
        data.put("total", page.getTotal());
        return Response.success(data, "获取成功", 0);
    }

    @Override
    public Response deleteAdmin(QueryVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        ManagerUsers managerUsers = getManagerUserById(queryByIdVO.getId());
        if (managerUsers == null) return Response.dataNotFoundErr("未查询到相关数据");
        LocalDateTime nowTime = LocalDateTime.now();
        managerUsers.setDeletedAt(nowTime);
        boolean res = updateById(managerUsers);
        if (!res) return Response.dataErr("删除失败,数据库异常");
        return Response.success("删除成功", managerUsers.getId());
    }

    @Override
    public Response getUserInfo(HttpServletRequest request) {
        ManagerUsers user = (ManagerUsers) request.getAttribute("user");
        return Response.success(ManagerUserDTO.toManagerUserTableDTO(user), "获取成功", user.getId());
    }

    @Override
    public Response validPassword(HttpServletRequest request, ManagerUsersVO managerUsersVO) {
        if (!Validator.isValidPassword(managerUsersVO.getPassword())) return Response.authErr("密码错误");
        ManagerUsers user = (ManagerUsers) request.getAttribute("user");
        if (!MD5.MD5Password(user.getAccount() + managerUsersVO.getPassword()).equals(user.getPassword()))
            return Response.authErr("密码错误");
        String token = MD5.MD5Password(user.getAccount() + request.getHeader("token"));
        String uid = MD5.MD5Password(request.getHeader("token") + user.getAccount());
        String res = RedisUtils.set(uid + "_resetPassword", token, 300);
        if (!Objects.equals(res, "OK")) return Response.systemErr("系统异常,请稍后再试");
        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("account", uid);
        return Response.success(data, "验证成功,请尽快修改密码,有效期五分钟", user.getId());
    }

    @Override
    public Response resetPassword(HttpServletRequest request, String pathParams, ManagerUsersVO managerUsersVO) {
        if (!Objects.equals(RedisUtils.get(pathParams + "_resetPassword"), managerUsersVO.getToken()))
            return Response.authErr("无效的链接");
        if (!Validator.isValidPassword(managerUsersVO.getPassword())) return Response.paramsErr("密码不合规范");
        RedisUtils.del(pathParams + "_resetPassword");
        ManagerUsers user = (ManagerUsers) request.getAttribute("user");
        user.setPassword(MD5.MD5Password(user.getAccount() + managerUsersVO.getPassword()));
        if (!updateById(user)) return Response.systemErr("修改失败,系统异常");
        String res = RedisUtils.set(user.getAccount(), JSONUtils.toJSONStr(user), 25200);
        if (!Objects.equals(res, "OK")) {
            long res1 = RedisUtils.del(user.getAccount());
        }
        return Response.success("重置成功", user.getId());
    }

    private ManagerUsers getManagerUserByAccount(String account) {
        QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("binary account", account);
        return managerUsersMapper.selectOne(queryWrapper);
    }

    private ManagerUsers getManagerUserById(Integer id) {
        QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return managerUsersMapper.selectOne(queryWrapper);
    }
}
