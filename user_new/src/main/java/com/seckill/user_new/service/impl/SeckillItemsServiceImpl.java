package com.seckill.user_new.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.dto.SeckillItemsDTO;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.controller.vo.SeckillRawDetailVO;
import com.seckill.user_new.entity.FinancialItems;
import com.seckill.user_new.entity.RedisService.SeckillRecordRedis;
import com.seckill.user_new.entity.RiskControl;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.FinancialItemsMapper;
import com.seckill.user_new.mapper.RiskControlMapper;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.service.ISeckillItemsService;
import com.seckill.user_new.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
@Service
public class SeckillItemsServiceImpl extends ServiceImpl<SeckillItemsMapper, SeckillItems> implements ISeckillItemsService {
    @Resource
    private SeckillItemsMapper seckillItemsMapper;
    @Resource
    private RiskControlMapper riskControlMapper;
    @Resource
    private FinancialItemsMapper financialItemsMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserServiceImpl userService;

    private User getTestUserByID(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return userMapper.selectOne(queryWrapper);
    }

    private SeckillItems getSeckillItemByID(Integer id) {
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return seckillItemsMapper.selectOne(queryWrapper);
    }

    /*
     * @MethodName getOverview
     * @author Wky1742095859
     * @Description 查询 order1=1 全部 2未开始 3进行中 4已结束
     * @Date 2022/4/7 23:22
     * @Param [pageVO]
     * @Return com.seckill.user_new.common.Response
     **/
    @Override
    public Response getOverview(PageVO pageVO) {
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        if (pageVO.getKeyWord() != null && !Validator.isValidProductName(pageVO.getKeyWord()))
            return Response.paramsErr("关键词异常");
        Page<SeckillItems> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        if (pageVO.getKeyWord() != null)
            queryWrapper.like("title", pageVO.getKeyWord());
        LocalDateTime nowTime = LocalDateTime.now();
        if (Objects.equals(pageVO.getOrder2(),2)) {
            queryWrapper.gt("start_time", nowTime);
        }
        if (Objects.equals(pageVO.getOrder2(),3)) {
            queryWrapper.le("start_time", nowTime);
            queryWrapper.ge("end_time", nowTime);
        }
        if (Objects.equals(pageVO.getOrder2(),4)) {
            queryWrapper.lt("end_time", nowTime);
        }
        queryWrapper.isNull("deleted_at");
        if (Objects.equals(pageVO.getOrder(),1)) {
            queryWrapper.orderByDesc("id");
        }
        seckillItemsMapper.selectPage(page, queryWrapper);
        List<SeckillItems> itemsList = page.getRecords();
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", SeckillItemsDTO.toSeckillItemsOverview(itemsList));
        data.put("total", page.getTotal());
        return Response.success(data, "获取成功");
    }

    @Override
    public Response getDetail(QueryVO queryVO) {
        if (queryVO.getId() == null) return Response.paramsErr("秒杀活动不存在");
        //布隆过滤器查询
        boolean res1 = RbloomFilterUtil.bloomFilterContains(RbloomFilterUtil.seckillBloomFilterName, queryVO.getId().toString());
        //黑名单查询
        Boolean res2 = RedisUtils.sismember("U:SeckillItem:blacklist", queryVO.getId().toString());
        if (res1 && Boolean.FALSE.equals(res2)) {
            SeckillRawDetailVO seckillRawDetailVO = new SeckillRawDetailVO();
            Map<String, String> map = RedisUtils.hgetall("U:SeckillItem:" + queryVO.getId());
            SeckillItems seckillItems;
            if (map == null || map.isEmpty()) {//查询mysql
                seckillItems = getSeckillItemByID(queryVO.getId());
                if (seckillItems == null) {
                    RedisUtils.sadd("U:SeckillItem:blacklist", queryVO.getId().toString());//加入黑名单
                    return Response.dataNotFoundErr("未查询到该商品");
                }
                map = JSONUtils.toRedisHash(seckillItems);
                RedisUtils.hset("U:SeckillItem:" + seckillItems.getId(), map);
            } else {
                seckillItems = JSONUtils.toEntity(map, SeckillItems.class);
            }
            if (seckillItems == null)
                return Response.systemErr("获取秒杀信息失败,系统异常");
            map = RedisUtils.hgetall("U:FinancialItem:" + seckillItems.getFinancialItemId());
            FinancialItems financialItems;
            if (map == null || map.isEmpty()) {//理财产品缓存不存在,查询mysql
                QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", seckillItems.getFinancialItemId());
                financialItems = financialItemsMapper.selectOne(queryWrapper);
                if (financialItems != null) {
                    map = JSONUtils.toRedisHash(financialItems);
                    RedisUtils.hset("U:FinancialItem:" + financialItems.getId(), map);
                }
            } else {
                financialItems = JSONUtils.toEntity(map, FinancialItems.class);
            }
            if (financialItems != null) {
                BeanUtil.copyProperties(financialItems, seckillRawDetailVO, true);
            }
            map = RedisUtils.hgetall("U:RiskControl:" + seckillItems.getRiskControlId());
            RiskControl riskControl;
            if (map == null || map.isEmpty()) {//风控配置不存在,查询mysql
                QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", seckillItems.getFinancialItemId());
                riskControl = riskControlMapper.selectOne(queryWrapper);
                if (riskControl != null) {
                    map = JSONUtils.toRedisHash(riskControl);
                    RedisUtils.hset("U:RiskControl:" + riskControl.getId(), map);
                }
            } else {
                riskControl = JSONUtils.toEntity(map, RiskControl.class);
            }
            if (riskControl != null) {
                BeanUtil.copyProperties(riskControl, seckillRawDetailVO, true);
            }
            BeanUtil.copyProperties(seckillItems, seckillRawDetailVO, true);
            return Response.success(seckillRawDetailVO, "OK");
        }
        return Response.paramsErr("秒杀活动不存在");
    }

    @Override
    public Response getSeckillLink(HttpServletRequest request, String seckillID) {
        if (seckillID == null) return Response.paramsErr("秒杀活动不存在");
        Boolean res = RedisUtils.exists("U:SeckillItem:" + seckillID);
        if (!Boolean.FALSE.equals(res)) {
            User user = (User) request.getAttribute("user");
            List<String> timeStr = RedisUtils.hmget("U:SeckillItem:" + seckillID, "startTime", "endTime", "amount");
            if (timeStr == null || timeStr.size() != 3)
                return Response.systemErr("系统异常");
            LocalDateTime startTime = LocalDateTime.parse(timeStr.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(timeStr.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime nowTime = LocalDateTime.now();
            if (startTime.isAfter(nowTime))
                return Response.systemErr("活动未开始");
            if (endTime.isBefore(nowTime))
                return Response.systemErr("活动已结束");
            SeckillRecordRedis seckillRecordRedis = new SeckillRecordRedis(user.getId(),
                    user.getPhone(), LocalDateTime.now(), new BigDecimal(timeStr.get(2)), 0,
                    Integer.parseInt(seckillID), Snowflake.nextLongID());
            String recordStr = JSONUtils.toJSONStr(seckillRecordRedis);
            String uid = UUIDUtil.getUUID();
            String res1 = RedisUtils.set("U:DoSeckill:" + uid, recordStr, 60);
            if (!Objects.equals(res1, "OK"))
                return Response.systemErr("秒杀失败,系统异常");
            return Response.success(uid, "OK");
        }
        return Response.dataNotFoundErr("秒杀活动不存在");
    }

    @Override
    public Response doSeckill(String seckillID) {
        String res = (String) RedisUtils.evalSHA(RedisUtils.doRechargeLuaSHA, Collections.singletonList("U:DoSeckill:" + seckillID), Collections.emptyList());
        if (res == null) {
            return Response.paramsErr("链接无效");
        }
        SeckillRecordRedis seckillRecordRedis = JSONUtils.toEntity(res, SeckillRecordRedis.class);
        if (seckillRecordRedis == null) {
            return Response.paramsErr("链接无效");
        }
        String riskId = RedisUtils.hget("U:SeckillItem:" + seckillRecordRedis.getSeckillItemsId(), "riskControlId");
        if (riskId == null)
            return Response.systemErr("系统异常");
        Long res1 = (Long) RedisUtils.evalSHA(RedisUtils.doSeckillLuaSHA,
                Arrays.asList("U:SeckillItem:" + seckillRecordRedis.getSeckillItemsId(), "remainingStock",
                        "U:User:" + seckillRecordRedis.getPhone(), "balance",
                        "U:UserBuy:" + seckillRecordRedis.getSeckillItemsId(), seckillRecordRedis.getPhone(),
                        "U:RiskControlRes:" + riskId),
                Arrays.asList("1", seckillRecordRedis.getAmount().toString()));
        Response response;
        if (res1 != null) {
            switch (res1.intValue()) {
                case 0:
                    response = Response.systemErr("系统异常,库存不存在");
                    break;
                case -1:
                    response = Response.systemErr("商品已售完");
                    break;
                case -2:
                    response = Response.systemErr("系统异常,用户余额不存在");
                    break;
                case -3:
                    response = Response.systemErr("系统异常,单价不存在");
                    break;
                case -4:
                    response = Response.systemErr("余额不足");
                    break;
                case -5:
                    response = Response.systemErr("已超过最大购买次数");
                    break;
                case -6:
                    response = Response.systemErr("初筛判断失败");
                    /*应查询mysql判断初筛结果,这里为了性能舍弃这一步骤,直接返回失败*/
                    break;
                case -7:
                    response = Response.systemErr("您没有购买资格");
                    break;
                case 1:
                    response = Response.success("已成功下单,正在处理中");
                    seckillRecordRedis.setStatus(1);
                    break;
                default:
                    response = Response.systemErr("系统异常");

            }
        } else {
            response = Response.systemErr("系统异常1");
        }
        Map<String, String> map = JSONUtils.toRedisHash(seckillRecordRedis);
        String task = RedisUtils.xadd("U:SeckillMessageQueue:queue", map, 100000, false);
        return response;
    }

    @Override
    public Response loadTest() {
        SeckillItems seckillItems = getSeckillItemByID(1);
        if (seckillItems == null) return Response.systemErr("初始化失败,未查询到测试活动");
        LocalDateTime nowTime = LocalDateTime.now();
        seckillItems.setUpdatedAt(nowTime);
        seckillItems.setStock(100000L);
        seckillItems.setRemainingStock(100000L);
        seckillItems.setAmount(new BigDecimal("10000"));
        if (!updateById(seckillItems)) return Response.systemErr("初始化活动保存mysql失败");
        Map<String, String> map = JSONUtils.toRedisHash(seckillItems);
        RedisUtils.hset("U:SeckillItem:" + seckillItems.getId(), map);//重新设置缓存
        RedisUtils.del("U:RiskControlRes:1");//删除原初筛结果
        RedisUtils.del("U:UserBuy:1");//删除购买次数记录
        User user = getTestUserByID(1);
        if (user == null) return Response.systemErr("初始化失败,未找到用户");
        user.setUpdatedAt(nowTime);
        user.setBalance(new BigDecimal("1100000000"));
        if (!userService.updateById(user)) return Response.systemErr("初始化保存user失败");
        RedisUtils.zadd("U:RiskControlRes:1", 1.0, user.getPhone());//初筛通过
        RedisUtils.hset("U:User:" + user.getPhone(), "balance", "1100000000");
        return Response.success("初始化完成");
    }

    @Override
    public Response getSeckillLinkTest(HttpServletRequest request, String seckillID){
        //测试版uid设为一小时
        if (seckillID == null) return Response.paramsErr("秒杀活动不存在");
        Boolean res = RedisUtils.exists("U:SeckillItem:" + seckillID);
        if (!Boolean.FALSE.equals(res)) {
            User user = (User) request.getAttribute("user");
            List<String> timeStr = RedisUtils.hmget("U:SeckillItem:" + seckillID, "startTime", "endTime", "amount");
            if (timeStr == null || timeStr.size() != 3)
                return Response.systemErr("系统异常");
            LocalDateTime startTime = LocalDateTime.parse(timeStr.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(timeStr.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime nowTime = LocalDateTime.now();
            if (startTime.isAfter(nowTime))
                return Response.systemErr("活动未开始");
            if (endTime.isBefore(nowTime))
                return Response.systemErr("活动已结束");
            SeckillRecordRedis seckillRecordRedis = new SeckillRecordRedis(user.getId(),
                    user.getPhone(), LocalDateTime.now(), new BigDecimal(timeStr.get(2)), 0,
                    Integer.parseInt(seckillID), Snowflake.nextLongID());
            String recordStr = JSONUtils.toJSONStr(seckillRecordRedis);
            String uid = UUIDUtil.getUUID();
            String res1 = RedisUtils.set("U:DoSeckill:" + uid, recordStr, 3600);
            if (!Objects.equals(res1, "OK"))
                return Response.systemErr("秒杀失败,系统异常");
            return Response.success(uid, "OK");
        }
        return Response.dataNotFoundErr("秒杀活动不存在");
    }
    @Override
    public Response doSeckillTest(String seckillID) {
        //测试版一个用户最高允许购买十万份
        String res = (String) RedisUtils.evalSHA(RedisUtils.doRechargeLuaSHA, Collections.singletonList("U:DoSeckill:" + seckillID), Collections.emptyList());
        if (res == null) {
            return Response.paramsErr("链接无效");
        }
        SeckillRecordRedis seckillRecordRedis = JSONUtils.toEntity(res, SeckillRecordRedis.class);
        if (seckillRecordRedis == null) {
            return Response.paramsErr("链接无效");
        }
        String riskId = RedisUtils.hget("U:SeckillItem:" + seckillRecordRedis.getSeckillItemsId(), "riskControlId");
        if (riskId == null)
            return Response.systemErr("系统异常");
        Long res1 = (Long) RedisUtils.evalSHA(RedisUtils.doSeckillLuaSHA,
                Arrays.asList("U:SeckillItem:" + seckillRecordRedis.getSeckillItemsId(), "remainingStock",
                        "U:User:" + seckillRecordRedis.getPhone(), "balance",
                        "U:UserBuy:" + seckillRecordRedis.getSeckillItemsId(), seckillRecordRedis.getPhone(),
                        "U:RiskControlRes:" + riskId),
                Arrays.asList("100000", seckillRecordRedis.getAmount().toString()));
        Response response;
        if (res1 != null) {
            switch (res1.intValue()) {
                case 0:
                    response = Response.systemErr("系统异常,库存不存在");
                    break;
                case -1:
                    response = Response.systemErr("商品已售完");
                    break;
                case -2:
                    response = Response.systemErr("系统异常,用户余额不存在");
                    break;
                case -3:
                    response = Response.systemErr("系统异常,单价不存在");
                    break;
                case -4:
                    response = Response.systemErr("余额不足");
                    break;
                case -5:
                    response = Response.systemErr("已超过最大购买次数");
                    break;
                case -6:
                    response = Response.systemErr("初筛判断失败");
                    /*应查询mysql判断初筛结果,这里为了性能舍弃这一步骤,直接返回失败*/
                    break;
                case -7:
                    response = Response.systemErr("您没有购买资格");
                    break;
                case 1:
                    response = Response.success("已成功下单,正在处理中");
                    seckillRecordRedis.setStatus(1);
                    break;
                default:
                    response = Response.systemErr("系统异常");

            }
        } else {
            response = Response.systemErr("系统异常1");
        }
        Map<String, String> map = JSONUtils.toRedisHash(seckillRecordRedis);
        String task = RedisUtils.xadd("U:SeckillMessageQueue:queue", map, 100000, false);
        return response;
    }
}
