package com.seckill.user_new.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.user_new.entity.RiskControl;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.mapper.RiskControlMapper;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Component
@EnableScheduling
@Async
@Transactional
public class LoadCacheTask {
    @Resource
    private SeckillItemsMapper seckillItemsMapper;
    @Resource
    private RiskControlMapper riskControlMapper;
    /*
     * @MethodName loadCache
     * @author Wky1742095859
     * @Description 商品预热
     * @Date 2022/4/12 23:21
     * @Param []
     * @Return void
     **/
    @Scheduled(fixedDelay = 3600000)
    public void loadItem() throws Exception {
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        LocalDateTime nowTime = LocalDateTime.now();
        queryWrapper.isNull("deleted_at").le("start_time", nowTime.plusHours(2)).ge("end_time", nowTime);
        List<SeckillItems> seckillItemsList = seckillItemsMapper.selectList(queryWrapper);
        if (seckillItemsList != null && !seckillItemsList.isEmpty()) {
            for (SeckillItems item :
                    seckillItemsList) {
                Map<String,String> seckillItemMap= JSONUtils.toRedisHash(item);
                Boolean res=RedisUtils.exists("U:SeckillItem:"+item.getId());
                if (res!=null && !res){
                    RedisUtils.hset("U:SeckillItem:"+item.getId(),seckillItemMap);
                }
            }
        }
    }
    /*
     * @MethodName loadCache
     * @author Wky1742095859
     * @Description 决策引擎加载
     * @Date 2022/4/12 23:21
     * @Param []
     * @Return void
     **/
    private void loadPolicy(SeckillItems items) throws Exception {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        LocalDateTime nowTime = LocalDateTime.now();
        queryWrapper.eq("id",items.getRiskControlId());
        RiskControl riskControl=riskControlMapper.selectOne(queryWrapper);
        if (riskControl!=null) {

        }
    }
}
