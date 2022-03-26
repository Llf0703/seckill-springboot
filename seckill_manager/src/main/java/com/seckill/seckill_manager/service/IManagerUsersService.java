package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
import com.seckill.seckill_manager.entity.ManagerUsers;

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
    Response editAdmin(ManagerUsersVO managerUsersVO);
    Response getAdmin(QueryByIdVO queryByIdVO);
    Response getAdminPage(PageVO pageVO);
}
