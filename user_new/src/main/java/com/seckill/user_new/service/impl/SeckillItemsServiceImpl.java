package com.seckill.user_new.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.dto.SeckillItemsDTO;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.service.ISeckillItemsService;
import com.seckill.user_new.utils.Validator;
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
 * @since 2022-04-07
 */
@Service
public class SeckillItemsServiceImpl extends ServiceImpl<SeckillItemsMapper, SeckillItems> implements ISeckillItemsService {
    @Resource
    private SeckillItemsMapper seckillItemsMapper;

    /*
     * @MethodName getOverview
     * @author Wky1742095859
     * @Description 查询 order1=1 全部 2未开始 3进行中 4已结束
     * @Date 2022/4/7 23:22
     * @Param [pageVO]
     * @Return com.seckill.user_new.common.Response
     **/
    @Override
    public Response getOverview(PageVO pageVO) {
        if (!Validator.isValidPageCurrent(pageVO.getCurrent())) return Response.paramsErr("页数异常");
        if (!Validator.isValidPageSize(pageVO.getSize())) return Response.paramsErr("请求数量超出范围");
        if (pageVO.getKeyWord() != null && !Validator.isValidProductName(pageVO.getKeyWord()))
            return Response.paramsErr("关键词异常");
        Page<SeckillItems> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        if (pageVO.getKeyWord() != null)
            queryWrapper.like("title", pageVO.getKeyWord());
        LocalDateTime nowTime = LocalDateTime.now();
        if (pageVO.getOrder2() == 2) {
            queryWrapper.gt("start_time", nowTime);
        }
        if (pageVO.getOrder2() == 3) {
            queryWrapper.le("start_time", nowTime);
            queryWrapper.ge("end_time", nowTime);
        }
        if (pageVO.getOrder2() == 4) {
            queryWrapper.lt("end_time", nowTime);
        }
        queryWrapper.isNull("deleted_at");
        if (pageVO.getOrder() == 1) {
            queryWrapper.orderByDesc("id");
        }
        seckillItemsMapper.selectPage(page, queryWrapper);
        List<SeckillItems> itemsList = page.getRecords();
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", SeckillItemsDTO.toSeckillItemsOverview(itemsList));
        data.put("total", page.getTotal());
        return Response.success(data, "获取成功");
    }

    @Override
    public Response getDetail(QueryVO queryVO) {
        return null;
    }
}
