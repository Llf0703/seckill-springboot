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
import com.seckill.user_new.utils.*;
import org.springframework.stereotype.Service;
import redis.clients.jedis.StreamEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        DoRecharge doRecharge = new DoRecharge(userId, rechargeVO.getAmount(), rechargeVO.getRechargeMethod(), null, Snowflake.nextLongID());
        String res = RedisUtils.set("U:DoRecharge:" + uuid, JSONUtils.toJSONStr(doRecharge), 300);//有效期五分钟
        if (!Objects.equals(res, "OK"))
            return Response.systemErr("获取二维码失败,系统异常");
        String img = QRCodeUtil.generatorQR(baseUrl + uuid);
        Dict data = Dict.create()
                .set("img", img)
                .set("url", baseUrl + uuid);
        return Response.success(data, "获取二维码成功,有效期五分钟");
    }

    @Override
    public Response doRecharge(String rechargeId) {
        if (rechargeId == null) return Response.paramsErr("充值失败,链接无效");
        String doRechargeStr = (String) RedisUtils.evalSHA(RedisUtils.doRechargeLuaSHA, Collections.singletonList("U:DoRecharge:" + rechargeId), Collections.emptyList());
        if (doRechargeStr == null) return Response.paramsErr("充值失败,链接无效");
        DoRecharge doRecharge = JSONUtils.toEntity(doRechargeStr, DoRecharge.class);
        if (doRecharge == null) return Response.paramsErr("充值失败,系统异常");
        doRecharge.setRechargeTime(LocalDateTime.now());
        Map<String, String> map = JSONUtils.toRedisHash(doRecharge);
        String id = RedisUtils.xadd("U:RechargeMessageQueue:", map, 100000, false);
        if (id == null) return Response.systemErr("充值失败,系统异常");
        /**
         byte[] idBytes;
         try {
         idBytes = RSAUtil.encryptByPublicKey(id.getBytes(StandardCharsets.UTF_8), RSAUtil.getPublicKey());
         } catch (Exception e) {
         return Response.success("扫码成功,正在处理中");
         }**/
        Dict data = Dict.create()
                .set("uid", id);
        return Response.success(data, "扫码成功,正在处理中");
    }

    @Override
    public Response getRechargeResult(String rechargeId) {
        List<StreamEntry> res = RedisUtils.xrange("U:RechargeMessageQueue:", rechargeId, rechargeId, 1);
        if (res == null || res.isEmpty()) {
            return Response.success("1");
        }
        return Response.success("0");
    }
}
