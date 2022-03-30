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
import com.seckill.seckill_manager.entity.SeckillItems;
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
        if (!Validator.isValidAmountCanNotBeZERO(amount) || stock <= 0) return Response.systemErr("数值无效");
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
        if (getSeckillItemById(id) == null) return Response.dataErr("invalid id");
        seckillItem.setUpdatedAt(localDateTime);
        if (!updateById(seckillItem)) return Response.systemErr("database error");
        return Response.success("success");
        //VO id不为空,查询数据是否存在,不存在返回dataErr,存在进行新增,同时修改更新时间
        //getSeckillItemById(itemVO.getId());
        /*
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setLenient(false);
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        SeckillItems item = SeckillItems.builder()
                .created_at(date)
                .updated_at(date)
                .title(itemVO.getTitle())
                .description(itemVO.getDescription())
                .amount(itemVO.getAmount())
                .stock(itemVO.getStock())
                .remaining_stock(itemVO.getStock())
                .build();
        try {
            Date start_time = fmt.parse(itemVO.getStart_time());
            Date end_time = fmt.parse(itemVO.getEnd_time());
            item = item.toBuilder()
                    .start_time(start_time)
                    .end_time(end_time)
                    .build();
        } catch (ParseException e) {
            return Response.systemErr("error");
        }
        seckillItemsMapper.insert(item);
        return Response.success("success");*/
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
}
