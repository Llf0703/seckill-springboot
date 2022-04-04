package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.dto.RiskControlDTO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.controller.vo.RiskControlVO;
import com.seckill.seckill_manager.entity.RiskControl;
import com.seckill.seckill_manager.mapper.RiskControlMapper;
import com.seckill.seckill_manager.service.IRiskControlService;
import com.seckill.seckill_manager.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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
        RiskControl query = getRiskControlByName(riskControlVO.getPolicyName());
        if (query != null) return Response.paramsErr("存在相同的决策名");
        if (riskControlVO.getId() == null) {
            BeanUtil.copyProperties(riskControlVO, riskControl, true);
            LocalDateTime localDateTime = LocalDateTime.now();
            riskControl.setCreatedAt(localDateTime);
            riskControl.setUpdatedAt(localDateTime);
            boolean res = save(riskControl);
            if (res) return Response.success("保存成功",riskControl.getId());
            return Response.dataErr("保存失败,数据库异常");
        }
        if (riskControlVO.getId() <= 0) return Response.dataNotFoundErr("保存失败,产品不存在");
        riskControl = getRiskControlById(riskControlVO.getId());
        if (riskControl == null) return Response.dataErr("保存失败,产品不存在");
        LocalDateTime localDateTime = LocalDateTime.now();
        BeanUtil.copyProperties(riskControlVO, riskControl, true);
        riskControl.setUpdatedAt(localDateTime);
        if (!updateById(riskControl)) return Response.dataErr("保存失败,数据库异常");
        return Response.success("保存成功",riskControl.getId());
    }

    @Override
    public Response getRiskControl(QueryVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        RiskControl riskControl = getRiskControlById(queryByIdVO.getId());
        if (riskControl == null) return Response.dataNotFoundErr("未查询到相关数据");
        RiskControlVO riskControlVO = new RiskControlVO();
        BeanUtil.copyProperties(riskControl, riskControlVO, true);
        return Response.success(riskControlVO, "获取成功",riskControl.getId());
    }

    @Override
    public Response getRiskControlPage(PageVO pageVO) {
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        Page<RiskControl> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").orderByDesc("id");;
        riskControlMapper.selectPage(page, queryWrapper);
        List<RiskControl> itemsList = page.getRecords();
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", RiskControlDTO.toRiskControlTableDTO(itemsList));
        data.put("total", page.getTotal());
        return Response.success(data, "获取成功",0);
    }

    @Override
    public Response deleteRiskControl(QueryVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        RiskControl riskControl = getRiskControlById(queryByIdVO.getId());
        if (riskControl == null) return Response.dataNotFoundErr("未查询到相关数据");
        LocalDateTime localDateTime = LocalDateTime.now();
        riskControl.setDeletedAt(localDateTime);
        if (!updateById(riskControl)) return Response.systemErr("数据库错误");
        return Response.success("删除成功",riskControl.getId());
    }

    private RiskControl getRiskControlById(Integer id) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return riskControlMapper.selectOne(queryWrapper);
    }

    private RiskControl getRiskControlByName(String name) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("binary policy_name", name);
        return riskControlMapper.selectOne(queryWrapper);
    }
}
