package com.seckill.user_new.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.RegisterVO;
import com.seckill.user_new.controller.vo.UserVO;
import com.seckill.user_new.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-05
 */
public interface IUserService extends IService<User> {
    Response LoginCheck(String ip);
    Response loginService(UserVO userVO, String ip);
    Response checkVersion(String token, String ip);
    Response loginOut(String token,String ip);
    Response register(RegisterVO registerVO,String ip);
    Response getUserInfo(HttpServletRequest request);
    Response getBalance(HttpServletRequest request);
}
