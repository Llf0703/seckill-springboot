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
import com.seckill.user_new.entity.RiskControl;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.mapper.FinancialItemsMapper;
import com.seckill.user_new.mapper.RiskControlMapper;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.service.ISeckillItemsService;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RbloomFilterUtil;
import com.seckill.user_new.utils.RedisUtils;
import com.seckill.user_new.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public Response getSeckillLink(String seckillID) {

        return null;
    }

    @Override
    public Response doSeckill(String seckillID) {
        return null;
    }
}
