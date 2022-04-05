package com.seckill.seckill_manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.dto.OperateRecordDTO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.entity.OperateRecord;
import com.seckill.seckill_manager.mapper.OperateRecordMapper;
import com.seckill.seckill_manager.service.IOperateRecordService;
import com.seckill.seckill_manager.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-04
 */
@Service
public class OperateRecordServiceImpl extends ServiceImpl<OperateRecordMapper, OperateRecord> implements IOperateRecordService {
    @Resource
    private OperateRecordMapper operateRecordMapper;

    @Override
    public Response getOperateRecordPage(PageVO pageVO) {
        if (!Validator.isValidZeroOrOne(pageVO.getOrder())) return Response.paramsErr("排序异常");
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        if (pageVO.getKeyWord() != null && !Validator.isValidAccount(pageVO.getKeyWord()))
            return Response.paramsErr("关键词异常");
        if (pageVO.getKeyWord2() != null && !Validator.isValidProductName(pageVO.getKeyWord2()))
            return Response.paramsErr("关键词异常");
        Page<OperateRecord> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<OperateRecord> queryWrapper = new QueryWrapper<>();
        if (pageVO.getKeyWord() != null)
            queryWrapper.like("manager_user_account", pageVO.getKeyWord());
        if (pageVO.getKeyWord2() != null)
            queryWrapper.like("operate", pageVO.getKeyWord2());
        queryWrapper.isNull("deleted_at");
        if (pageVO.getOrder() == 1) {
            queryWrapper.orderByDesc("id");
        }
        operateRecordMapper.selectPage(page, queryWrapper);
        List<OperateRecord> itemsList = page.getRecords();
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", OperateRecordDTO.toOperateRecordTableDTO(itemsList));
        data.put("total", page.getTotal());
        return Response.success(data, "获取成功", 0);
    }
}
