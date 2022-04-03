package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.dto.SeckillItemDTO;
import com.seckill.seckill_manager.controller.vo.PageVO;
import com.seckill.seckill_manager.controller.vo.QueryByIdVO;
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
        if (!Validator.isValidAmountCanNotBeZERO(amount) || stock == null || stock <= 0)
            return Response.systemErr("数值无效");
        if (itemVO.getFinancialItemId() == null ||
                itemVO.getFinancialItemId() <= 0 ||
                itemVO.getRiskControlId() == null ||
                itemVO.getRiskControlId() <= 0)
            return Response.paramsErr("请输入正确的理财产品或风险引擎");
        SeckillItems seckillItem = new SeckillItems();
        BeanUtil.copyProperties(itemVO, seckillItem, true);//复制属性
        //VO id为空,设置更新,创建时间,进行新增
        LocalDateTime localDateTime = LocalDateTime.now();
        Integer id = seckillItem.getId();
        if (id == null) {
            seckillItem.setCreatedAt(localDateTime);//初始化创建时间
            seckillItem.setUpdatedAt(localDateTime);//初始化更新时间
            boolean res = save(seckillItem);
            if (res) return Response.success("success");
            return Response.systemErr("database error");
        }
        if (id <= 0) return Response.dataErr("invalid id");
        if (getSeckillItemById(id) == null ||
                getFinancialItemsById(itemVO.getFinancialItemId()) == null ||
                getRiskControlById(itemVO.getRiskControlId()) == null)
            return Response.dataErr("invalid id");
        seckillItem.setUpdatedAt(localDateTime);
        if (!updateById(seckillItem)) return Response.systemErr("database error");
        return Response.success("success");
    }

    @Override
    public Response getSeckillItem(QueryByIdVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        SeckillItems seckillItems = getSeckillItemById(queryByIdVO.getId());
        if (seckillItems == null) return Response.dataNotFoundErr("未查询到相关数据");
        return Response.success(SeckillItemDTO.toSeckillPostFormDTO(seckillItems), "获取成功");
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
        return Response.success(SeckillItemDTO.toSeckillItemTableDTO(itemsList), "获取成功");
    }

    @Override
    public Response deleteSeckillItemPage(QueryByIdVO queryByIdVO) {
        if (queryByIdVO.getId() == null || queryByIdVO.getId() <= 0) return Response.paramsErr("参数异常");
        SeckillItems seckillItems = getSeckillItemById(queryByIdVO.getId());
        if (seckillItems == null) return Response.dataNotFoundErr("未查询到相关数据");
        int res = seckillItemsMapper.deleteById(seckillItems);
        System.out.println(res);
        return Response.success(String.valueOf(res));
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
