package com.seckill.user_new.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.RechargeVO;
import com.seckill.user_new.entity.RechargeRecord;
import com.seckill.user_new.entity.RedisService.DoRecharge;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.RechargeRecordMapper;
import com.seckill.user_new.service.IRechargeRecordService;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.QRCodeUtil;
import com.seckill.user_new.utils.RedisUtils;
import com.seckill.user_new.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-08
 */
@Service
public class RechargeRecordServiceImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecord> implements IRechargeRecordService {
    /*
     * @MethodName getRechargeLink
     * @author Wky1742095859
     * @Description 获取充值二维码
     * @Date 2022/4/8 1:50
     * @Param [user, rechargeVO, baseUrl]
     * @Return com.seckill.user_new.common.Response
    **/
    @Override
    public Response getRechargeLink(User user, RechargeVO rechargeVO, String baseUrl) {
        if (rechargeVO.getAmount() == null) return Response.paramsErr("金额异常");
        if (rechargeVO.getAmount().compareTo(BigDecimal.ZERO) <= 0 ||
                rechargeVO.getAmount().compareTo(new BigDecimal("99999.9999")) > 0)
            return Response.paramsErr("金额超出范围");
        if (rechargeVO.getRechargeMethod() == null ||
                rechargeVO.getRechargeMethod() > 4 ||
                rechargeVO.getRechargeMethod() < 1)
            return Response.paramsErr("充值方式异常");
        Integer userId = user.getId();
        String uuid = UUIDUtil.getUUID();
        DoRecharge doRecharge=new DoRecharge(userId,rechargeVO.getAmount(), rechargeVO.getRechargeMethod(), null);
        String res= RedisUtils.set("U:DoRecharge:"+uuid, JSONUtils.toJSONStr(doRecharge),300);//有效期五分钟
        if (!Objects.equals(res,"OK"))
            return Response.systemErr("获取二维码失败,系统异常");
        String img= QRCodeUtil.generatorQR(baseUrl+uuid);
        Dict data=Dict.create().set("img",img);
        return Response.success(data, "获取二维码成功,有效期五分钟");
    }

    @Override
    public Response doRecharge(String rechargeId) {
        if (rechargeId == null) return Response.paramsErr("充值失败,无效的连接");
        String doRechargeStr = RedisUtils.get("U:DoRecharge:" + rechargeId);
        if (doRechargeStr == null) return Response.paramsErr("充值失败,无效的连接");
        DoRecharge doRecharge = JSONUtils.toEntity(doRechargeStr, DoRecharge.class);
        if (doRecharge == null) return Response.paramsErr("充值失败,系统异常");
        Dict res = Dict.create()
                .set("amount", doRecharge.getAmount())
                .set("rechargeMethod", doRecharge.getRechargeMethod())
                .set("rechargeTime", LocalDateTime.now());
        return Response.success(res, "充值成功");
    }

    @Override
    public Response getRechargeResult(String rechargeId) {
        return null;
    }
}
