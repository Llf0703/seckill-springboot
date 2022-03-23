package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.entity.FinancialItems;
import com.seckill.seckill_manager.mapper.FinancialItemsMapper;
import com.seckill.seckill_manager.service.IFinancialItemsService;
import com.seckill.seckill_manager.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
        if (!Validator.isValidShelfLife(financialItemVO.getShelfLife()))
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
        if (financialItemVO.getProductEffectiveDate().isBefore(financialItemVO.getProductExpirationDate()))
            return Response.paramsErr("产品失效日期应大于产品生效日期");
        FinancialItems financialItem = new FinancialItems();
        if (financialItemVO.getId() == null) {//id不存在,新增数据
            BeanUtil.copyProperties(financialItemVO, financialItem, true);
            LocalDateTime localDateTime = LocalDateTime.now();
            financialItem.setCreatedAt(localDateTime);
            financialItem.setUpdatedAt(localDateTime);
            save(financialItem);
            return Response.success("新增成功");
        }
        financialItem = getFinancialItemById(financialItemVO);
        if (financialItem == null) return Response.dataErr("未找到该商品");
        LocalDateTime localDateTime = LocalDateTime.now();
        BeanUtil.copyProperties(financialItemVO,financialItem,true);
        financialItem.setUpdatedAt(localDateTime);
        if (!updateById(financialItem)) return Response.dataErr("保存失败");
        return Response.success("保存成功");
    }

    private FinancialItems getFinancialItemById(FinancialItemVO financialItemVO) {
        if (financialItemVO.getId() == null) return null;
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", financialItemVO.getId());
        return financialItemsMapper.selectOne(queryWrapper);
    }
}
