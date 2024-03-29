package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.entity.ManagerUsers;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-19
 */
public interface IManagerUsersService extends IService<ManagerUsers> {
    Response LoginCheck(String ip);
    Response login(ManagerUsersVO managerUsersVO, String ip, HttpServletRequest request);
    Response checkVersion(String token, String ip);
    Response loginOut(String token,String ip);
    Response editAdmin(ManagerUsersVO managerUsersVO);
    Response getAdmin(QueryVO queryByIdVO);
    Response getAdminPage(PageVO pageVO);
    Response deleteAdmin(QueryVO queryByIdVO);
    Response getUserInfo(HttpServletRequest request);
    Response validPassword(HttpServletRequest request,ManagerUsersVO managerUsersVO);
    Response resetPassword(HttpServletRequest request,String pathParams,ManagerUsersVO managerUsersVO);
}
