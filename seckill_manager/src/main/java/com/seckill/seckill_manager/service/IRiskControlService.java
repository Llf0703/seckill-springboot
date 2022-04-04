package com.seckill.seckill_manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryVO;
import com.seckill.seckill_manager.controller.vo.RiskControlVO;
import com.seckill.seckill_manager.entity.RiskControl;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-24
 */
public interface IRiskControlService extends IService<RiskControl> {
    Response editRiskControl(RiskControlVO riskControlVO);
    Response getRiskControl(QueryVO queryByIdVO);
    Response getRiskControlPage(PageVO pageVO);
    Response deleteRiskControl(QueryVO queryByIdVO);
}
