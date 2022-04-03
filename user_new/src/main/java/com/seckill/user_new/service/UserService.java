package com.seckill.user_new.service;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.UserVO;

public interface UserService {
    public Response LoginCheck(String ip);
    public Response loginService(UserVO userVO, String ip);
}
