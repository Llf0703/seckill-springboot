package com.seckill.seckill_manager.service;

import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.entity.OperateRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-04
 */
public interface IOperateRecordService extends IService<OperateRecord> {
    Response getOperateRecordPage(PageVO pageVO);
}
