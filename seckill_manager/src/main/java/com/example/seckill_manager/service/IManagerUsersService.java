package com.example.seckill_manager.service;

import com.example.seckill_manager.common.Response;
import com.example.seckill_manager.controller.vo.ManagerUsersVO;
import com.example.seckill_manager.entity.ManagerUsers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-19
 */
public interface IManagerUsersService extends IService<ManagerUsers> {
    Response login(ManagerUsersVO managerUsersVO,String ip);
    Response checkVersion(String token,String ip);
    Response loginOut(String token,String ip);
}
