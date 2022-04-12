package com.seckill.user_new.controller;


import com.seckill.user_new.Interceptor.Type.LoginRequired;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.RechargeVO;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.service.impl.RechargeRecordServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-08
 */
@RestController
@RequestMapping("/api")
public class RechargeRecordController {
    @Resource
    private RechargeRecordServiceImpl rechargeRecordService;

    /*
     * @MethodName getRechargeLink
     * @author Wky1742095859
     * @Description 生成充值二维码,需登录才能充值
     * @Date 2022/4/8 0:48
     * @Param [request, rechargeVO]
     * @Return com.seckill.user_new.common.Response
     **/
    @LoginRequired
    @PostMapping("/recharge/get_recharge_link")
    public Response getRechargeLink(HttpServletRequest request, @RequestBody RechargeVO rechargeVO) {
        User user = (User) request.getAttribute("user");
        String baseUrl = "http://124.223.199.137:7777/api/recharge/do_recharge/";
        return rechargeRecordService.getRechargeLink(user, rechargeVO, baseUrl);
    }

    /*
     * @MethodName doRecharge
     * @author Wky1742095859
     * @Description 进行充值
     * @Date 2022/4/8 0:59
     * @Param [rechargeId]
     * @Return com.seckill.user_new.common.Response
     **/
    @GetMapping("/recharge/do_recharge/{rechargeId}")
    public Response doRecharge(@PathVariable("rechargeId") String rechargeId) {
        return rechargeRecordService.doRecharge(rechargeId);
    }

    /*
     * @MethodName getRechargeResult
     * @author Wky1742095859
     * @Description 获取充值结果
     * @Date 2022/4/8 0:59
     * @Param [rechargeId]
     * @Return com.seckill.user_new.common.Response
     **/
    @LoginRequired
    @PostMapping("/recharge/get_recharge_result/{rechargeId}")
    public Response getRechargeResult(@PathVariable("rechargeId") String rechargeId) {
        return rechargeRecordService.getRechargeResult(rechargeId);
    }
}
