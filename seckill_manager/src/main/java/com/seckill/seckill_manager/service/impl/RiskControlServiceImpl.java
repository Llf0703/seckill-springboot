package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
import com.seckill.seckill_manager.controller.vo.RiskControlVO;
import com.seckill.seckill_manager.entity.RiskControl;
import com.seckill.seckill_manager.mapper.RiskControlMapper;
import com.seckill.seckill_manager.service.IRiskControlService;
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
 * @since 2022-03-24
 */
@Service
public class RiskControlServiceImpl extends ServiceImpl<RiskControlMapper, RiskControl> implements IRiskControlService {
    @Resource
    private RiskControlMapper riskControlMapper;

    @Override
    public Response editRiskControl(RiskControlVO riskControlVO) {
        if (!Validator.isValidProductName(riskControlVO.getPolicyName())) return Response.paramsErr("决策引擎名称不合规");
        if (!Validator.isValidZeroOrOne(riskControlVO.getWorkingStatusLimit()))
            return Response.paramsErr("错误的失业人员限制选项");
        if (!Validator.isValidZeroOrOne(riskControlVO.getUntrustworthyPersonLimit()))
            return Response.paramsErr("错误的失信人员限制选项");
        if (!Validator.isValidAge(riskControlVO.getAgeLimit())) return Response.paramsErr("年龄限制超出范围");
        if (!Validator.isValidAge(riskControlVO.getOverdueYearLimit())) return Response.paramsErr("逾期年份限制超出范围");
        if (!Validator.isValidTimes(riskControlVO.getOverdueNumberLimit())) return Response.paramsErr("逾期次数超出范围");
        if (!Validator.isValidAmountCanNotBeZERO(riskControlVO.getExceptionAmount()))
            return Response.paramsErr("例外金额超出范围");
        if (!Validator.isValidDays(riskControlVO.getExceptionDays())) return Response.paramsErr("例外天数超出范围");
        RiskControl riskControl = new RiskControl();
        if (riskControlVO.getId() == null) {
            BeanUtil.copyProperties(riskControlVO, riskControl, true);
            LocalDateTime localDateTime = LocalDateTime.now();
            riskControl.setCreatedAt(localDateTime);
            riskControl.setUpdatedAt(localDateTime);
            boolean res = save(riskControl);
            if (res) return Response.success("保存成功");
            return Response.dataErr("保存失败,数据库异常");
        }
        riskControl = getRiskControlById(riskControlVO.getId());
        if (riskControl == null) return Response.dataErr("保存失败,未找到该产品");
        LocalDateTime localDateTime = LocalDateTime.now();
        BeanUtil.copyProperties(riskControlVO, riskControl, true);
        riskControl.setUpdatedAt(localDateTime);
        if (!updateById(riskControl)) return Response.dataErr("保存失败,数据库异常");
        return Response.success("保存成功");
    }

    @Override
    public Response getRiskControl(QueryByIdVO queryByIdVO) {
        if (queryByIdVO.getId() == null) return Response.paramsErr("参数异常");
        RiskControl riskControl = getRiskControlById(queryByIdVO.getId());
        if (riskControl == null) return Response.dataNotFoundErr("未查询到相关数据");
        RiskControlVO riskControlVO = new RiskControlVO();
        BeanUtil.copyProperties(riskControl, riskControlVO, true);
        return Response.success(riskControlVO, "获取成功");
    }

    private RiskControl getRiskControlById(Integer id) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return riskControlMapper.selectOne(queryWrapper);
    }
}
