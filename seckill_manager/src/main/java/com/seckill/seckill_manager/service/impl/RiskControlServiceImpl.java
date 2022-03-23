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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        return Response.success("test");
    }

    @Override
    public Response getRiskControl(QueryByIdVO queryByIdVO){
        if (queryByIdVO.getId()==null)return Response.paramsErr("参数异常");
        RiskControl riskControl=getRiskControlById(queryByIdVO.getId());
        if (riskControl==null)return Response.dataNotFoundErr("未查询到相关数据");
        RiskControlVO riskControlVO=new RiskControlVO();
        BeanUtil.copyProperties(riskControl,riskControlVO,true);
        return Response.success(riskControlVO,"获取成功");
    }

    private RiskControl getRiskControlById(Integer id) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", id);
        return riskControlMapper.selectOne(queryWrapper);
    }
}
