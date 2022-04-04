package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.dto.FinancialItemDTO;
import com.seckill.seckill_manager.controller.dto.RiskControlDTO;
import com.seckill.seckill_manager.controller.dto.SeckillItemDTO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.controller.vo.SeckillItemVO;
import com.seckill.seckill_manager.entity.FinancialItems;
import com.seckill.seckill_manager.entity.RiskControl;
import com.seckill.seckill_manager.entity.SeckillItems;
import com.seckill.seckill_manager.mapper.FinancialItemsMapper;
import com.seckill.seckill_manager.mapper.RiskControlMapper;
import com.seckill.seckill_manager.mapper.SeckillItemsMapper;
import com.seckill.seckill_manager.service.ISeckillItemsService;
import com.seckill.seckill_manager.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
@Service
public class SeckillItemsServiceImpl extends ServiceImpl<SeckillItemsMapper, SeckillItems> implements ISeckillItemsService {
    @Resource
    private SeckillItemsMapper seckillItemsMapper;
    @Resource
    private RiskControlMapper riskControlMapper;
    @Resource
    private FinancialItemsMapper financialItemsMapper;

    /*
     * @MethodName editSeckillItem
     * @author llf
     * @Description 新增,修改秒杀产品接口
     * @Date 2022/3/23 23:10
     * @Param [itemVO]
     * @Return com.seckill.seckill_manager.common.Response
     **/
    @Override
    public Response editSeckillItem(SeckillItemVO itemVO) {
        BigDecimal amount = itemVO.getAmount();
        Long stock = itemVO.getStock();
        if (!Validator.isValidProductName(itemVO.getTitle())) return Response.paramsErr("活动名不合法");
        if (!Validator.isValidDescription(itemVO.getDescription())) return Response.paramsErr("活动描述不合法");
        if (!Validator.isValidAmountCanNotBeZERO(amount) || stock == null || stock <= 0 || stock > 9999999999L)
            return Response.systemErr("数值无效");
        if (itemVO.getFinancialItemId() == null || itemVO.getFinancialItemId() <= 0 || itemVO.getRiskControlId() == null || itemVO.getRiskControlId() <= 0)
            return Response.paramsErr("请输入正确的理财产品或风险引擎");
        if (!Validator.isValidSeckillTime(itemVO.getStartTime(), itemVO.getEndTime()))
            Response.paramsErr("开始时间或结束时间异常");
        if (getRiskControlById(itemVO.getRiskControlId()) == null) return Response.paramsErr("不存在该决策引擎");
        if (getFinancialItemsById(itemVO.getFinancialItemId()) == null) return Response.paramsErr("不存在该理财产品");
        SeckillItems seckillItem = new SeckillItems();
        BeanUtil.copyProperties(itemVO, seckillItem, true);//复制属性
        //VO id为空,设置更新,创建时间,进行新增
        LocalDateTime localDateTime = LocalDateTime.now();
        seckillItem.setRemainingStock(seckillItem.getStock());//更新剩余库存
        seckillItem.setUpdatedAt(localDateTime);//初始化更新时间
        Integer id = seckillItem.getId();
        if (id == null) {
            seckillItem.setCreatedAt(localDateTime);//初始化创建时间
            boolean res = save(seckillItem);
            if (res) return Response.success("添加成功",seckillItem.getId());
            return Response.systemErr("database error");
        }
        if (id <= 0) return Response.dataErr("invalid id");
        SeckillItems query=getSeckillItemById(id);
        if (query == null)
            return Response.dataErr("invalid id");
        if (!query.getStartTime().isBefore(LocalDateTime.now().plusHours(-2)))
            return Response.paramsErr("活动已开始或距开始小于两小时,无法修改");
        if (!updateById(seckillItem)) return Response.systemErr("database error");
        return Response.success("修改成功",seckillItem.getId());
    }

    @Override
    public Response getSeckillItem(QueryVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        SeckillItems seckillItems = getSeckillItemById(queryByIdVO.getId());
        if (seckillItems == null) return Response.dataNotFoundErr("未查询到相关数据");
        return Response.success(SeckillItemDTO.toSeckillPostFormDTO(seckillItems), "获取成功", seckillItems.getId());
    }

    @Override
    public Response getSeckillItemPage(PageVO pageVO) {
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        Page<SeckillItems> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at");
        seckillItemsMapper.selectPage(page, queryWrapper);
        List<SeckillItems> itemsList = page.getRecords();
        HashMap<String,Object> data=new HashMap<>();
        data.put("items",SeckillItemDTO.toSeckillItemTableDTO(itemsList));
        data.put("total",page.getTotal());
        return Response.success(data, "获取成功",0);
    }

    @Override
    public Response deleteSeckillItemPage(QueryVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        SeckillItems seckillItems = getSeckillItemById(queryByIdVO.getId());
        if (seckillItems == null) return Response.dataNotFoundErr("未查询到相关数据");
        LocalDateTime nowTime=LocalDateTime.now();
        seckillItems.setDeletedAt(nowTime);
        boolean res = updateById(seckillItems);
        if (!res)return Response.dataErr("删除失败,数据库异常");
        return Response.success("删除成功",seckillItems.getId());
    }

    @Override
    public Response searchFinancialItemOptions(QueryVO queryByNameVO) {
        if (queryByNameVO.getKeyWord() == null) return null;
        if (!Validator.isValidProductName(queryByNameVO.getKeyWord())) return Response.success("OK",0);
        Page<FinancialItems> page = new Page<>(1, 25);
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").like("product_name", queryByNameVO.getKeyWord());
        financialItemsMapper.selectPage(page, queryWrapper);
        List<FinancialItems> itemsList = page.getRecords();
        return Response.success(FinancialItemDTO.toFinancialItemOptionsDTO(itemsList), "OK",0);
    }

    @Override
    public Response searchRiskControlOptions(QueryVO queryByNameVO) {
        if (queryByNameVO.getKeyWord() == null) return null;
        if (!Validator.isValidProductName(queryByNameVO.getKeyWord())) return Response.success("OK",0);
        Page<RiskControl> page = new Page<>(1, 25);
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").like("policy_name", queryByNameVO.getKeyWord());
        riskControlMapper.selectPage(page, queryWrapper);
        List<RiskControl> itemsList = page.getRecords();
        return Response.success(RiskControlDTO.toRiskControlOptionsDTO(itemsList), "OK",0);
    }

    private SeckillItems getSeckillItemById(Integer id) {
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return seckillItemsMapper.selectOne(queryWrapper);
    }

    private FinancialItems getFinancialItemsById(Integer id) {
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return financialItemsMapper.selectOne(queryWrapper);
    }

    private RiskControl getRiskControlById(Integer id) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return riskControlMapper.selectOne(queryWrapper);
    }


}
