package com.seckill.seckill_manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.SeckillItemVO;
import com.seckill.seckill_manager.entity.SeckillItems;
import com.seckill.seckill_manager.mapper.ManagerUsersMapper;
import com.seckill.seckill_manager.mapper.SeckillItemsMapper;
import com.seckill.seckill_manager.service.ISeckillItemsService;
import com.seckill.seckill_manager.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class SeckillItemsServiceImpl extends ServiceImpl<SeckillItemsMapper, SeckillItems> implements ISeckillItemsService {
    @Resource
    private SeckillItemsMapper seckillItemsMapper;

    @Resource
    private ManagerUsersMapper managerUsersMapper;

    @Override
    public Response editItem(SeckillItemVO item_VO) {
        BigDecimal amount = item_VO.getAmount();
        Long stock = item_VO.getStock();
        if (!Validator.isValidAmountCanNotBeZERO(amount) || stock <= 0) return Response.systemErr("数值无效");
        SeckillItems seckillItem = new SeckillItems();
        BeanUtil.copyProperties(item_VO,seckillItem,true);//复制属性
        //VO id为空,设置更新,创建时间,进行新增
        LocalDateTime localDateTime = LocalDateTime.now();
        seckillItem.setCreatedAt(localDateTime);//初始化创建时间
        seckillItem.setUpdatedAt(localDateTime);//初始化更新时间
        save(seckillItem);
        return Response.success("test");
        //VO id不为空,查询数据是否存在,不存在返回dataErr,存在进行新增,同时修改更新时间
        //getSeckillItemById(item_VO);
        /*
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setLenient(false);
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        SeckillItems item = SeckillItems.builder()
                .created_at(date)
                .updated_at(date)
                .title(item_VO.getTitle())
                .description(item_VO.getDescription())
                .amount(item_VO.getAmount())
                .stock(item_VO.getStock())
                .remaining_stock(item_VO.getStock())
                .build();
        try {
            Date start_time = fmt.parse(item_VO.getStart_time());
            Date end_time = fmt.parse(item_VO.getEnd_time());
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

    private SeckillItems getSeckillItemById(SeckillItemVO seckillItemVO){
        if (seckillItemVO.getId() == null) return null;
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at").eq("id", seckillItemVO.getId());
        return seckillItemsMapper.selectOne(queryWrapper);
    }
}
