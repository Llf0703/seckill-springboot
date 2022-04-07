package com.seckill.user_new.service;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.RechargeVO;
import com.seckill.user_new.entity.RechargeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.user_new.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-08
 */
public interface IRechargeRecordService extends IService<RechargeRecord> {
    Response getRechargeLink(User user, RechargeVO rechargeVO,String baseUrl);
    Response doRecharge(String rechargeId);
    Response getRechargeResult(String rechargeId);
}
