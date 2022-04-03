package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.dto.FinancialItemDTO;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
import com.seckill.seckill_manager.entity.FinancialItems;
import com.seckill.seckill_manager.mapper.FinancialItemsMapper;
import com.seckill.seckill_manager.service.IFinancialItemsService;
import com.seckill.seckill_manager.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
public class FinancialItemsServiceImpl extends ServiceImpl<FinancialItemsMapper, FinancialItems> implements IFinancialItemsService {
    @Resource
    private FinancialItemsMapper financialItemsMapper;

    @Override
    public Response editFinancialItem(FinancialItemVO financialItemVO) {
        if (!Validator.isValidAmountCanNotBeZERO(financialItemVO.getDailyCumulativeLimit()))
            return Response.paramsErr("日累计限额超出范围");
        if (!Validator.isValidProductName(financialItemVO.getProductName()))
            return Response.paramsErr("产品名称不合规");
        if (!Validator.isValidZeroOrOne(financialItemVO.getAutomaticRedemptionAtMaturity()))
            return Response.paramsErr("错误的到期自动赎回选项");
        if (!Validator.isValidAmountCanBeZERO(financialItemVO.getIncrementAmount()))
            return Response.paramsErr("递增金额超出范围");
        if (!Validator.isValidAmountCanNotBeZERO(financialItemVO.getMaximumSinglePurchaseAmount()))
            return Response.paramsErr("单笔最大金额超出范围");
        if (!Validator.isValidAmountCanNotBeZERO(financialItemVO.getMinimumDepositAmount()))
            return Response.paramsErr("起存金额超出范围");
        if (!Validator.isValidZeroOrOne(financialItemVO.getEarlyWithdrawal()))
            return Response.paramsErr("错误的允许提前支取选项");
        if (!Validator.isValidRate(financialItemVO.getInterestRate()))
            return Response.paramsErr("利率超出范围");
        if (!Validator.isValidDays(financialItemVO.getShelfLife()))
            return Response.paramsErr("存期超出范围");
        if (financialItemVO.getMinimumDepositAmount().compareTo(financialItemVO.getMaximumSinglePurchaseAmount()) > 0)
            return Response.paramsErr("起存金额不能大于单笔最大金额");
        if (financialItemVO.getMinimumDepositAmount().compareTo(financialItemVO.getDailyCumulativeLimit()) > 0)
            return Response.paramsErr("起存金额不能大于日累计限额");
        if (financialItemVO.getMaximumSinglePurchaseAmount().compareTo(financialItemVO.getDailyCumulativeLimit()) > 0)
            return Response.paramsErr("单笔最大金额不能大于日累计限额");
        if (financialItemVO.getProductExpirationDate() == null)
            return Response.paramsErr("产品失效日期不能为空");
        if (financialItemVO.getProductEffectiveDate() == null)
            return Response.paramsErr("产品生效日期不能为空");
        if (!financialItemVO.getProductEffectiveDate().isBefore(financialItemVO.getProductExpirationDate()))
            return Response.paramsErr("产品失效日期应大于产品生效日期");
        FinancialItems financialItem = new FinancialItems();
        FinancialItems query = getFinancialItemByName(financialItemVO.getProductName());
        if (query != null) return Response.dataErr("存在相同名称的产品");
        if (financialItemVO.getId() == null) {//id不存在,新增数据
            BeanUtil.copyProperties(financialItemVO, financialItem, true);
            LocalDateTime localDateTime = LocalDateTime.now();
            financialItem.setCreatedAt(localDateTime);//初始化创建时间
            financialItem.setUpdatedAt(localDateTime);//初始化更新时间
            //百分数化小数
            financialItem.setInterestRate(financialItemVO.getInterestRate().divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
            boolean res = save(financialItem);
            if (res) return Response.success("新增成功");
            return Response.dataErr("保存失败,数据库异常");
        }
        //id存在,修改数据
        if (financialItemVO.getId() <= 0) return Response.dataErr("保存失败,产品不存在");
        financialItem = getFinancialItemById(financialItemVO.getId());
        if (financialItem == null) return Response.dataErr("保存失败,产品不存在");
        LocalDateTime localDateTime = LocalDateTime.now();
        BeanUtil.copyProperties(financialItemVO, financialItem, true);//更改字段
        financialItem.setUpdatedAt(localDateTime);//修改更新时间
        //百分数化小数
        financialItem.setInterestRate(financialItemVO.getInterestRate().divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
        if (!updateById(financialItem)) return Response.dataErr("保存失败,数据库异常");
        return Response.success("保存成功");
    }

    @Override
    public Response getFinancialItem(QueryByIdVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        FinancialItems financialItem = getFinancialItemById(queryByIdVO.getId());
        if (financialItem == null) return Response.dataNotFoundErr("产品不存在");
        return Response.success(FinancialItemDTO.toFinancialItemPostFormDTO(financialItem), "获取成功");
    }

    @Override
    public Response getFinancialItemPage(PageVO pageVO) {
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        Page<FinancialItems> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at");
        financialItemsMapper.selectPage(page, queryWrapper);
        List<FinancialItems> itemsList = page.getRecords();
        return Response.success(FinancialItemDTO.toFinancialItemTableDTO(itemsList), "获取成功");
    }

    @Override
    public Response deleteFinancialItem(QueryByIdVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        FinancialItems financialItem = getFinancialItemById(queryByIdVO.getId());
        if (financialItem == null) return Response.dataNotFoundErr("产品不存在");
        LocalDateTime localDateTime = LocalDateTime.now();
        financialItem.setDeletedAt(localDateTime);
        if (!updateById(financialItem)) return Response.systemErr("数据库错误");
        return Response.success("删除成功");
    }

    private FinancialItems getFinancialItemById(Integer id) {
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return financialItemsMapper.selectOne(queryWrapper);
    }

    private FinancialItems getFinancialItemByName(String name) {
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("product_name", name);
        return financialItemsMapper.selectOne(queryWrapper);
    }
}
