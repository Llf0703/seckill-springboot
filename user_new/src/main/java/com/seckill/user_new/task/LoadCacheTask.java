package com.seckill.user_new.task;

import cn.hutool.core.util.IdcardUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seckill.user_new.entity.*;
import com.seckill.user_new.mapper.*;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    @Resource
    private OverdueRecordMapper overdueRecordMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private FinancialItemsMapper financialItemsMapper;

    private RiskControl getRiskControlByID(Integer id) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return riskControlMapper.selectOne(queryWrapper);
    }

    private FinancialItems getFinancialItemByID(Integer id) {
        QueryWrapper<FinancialItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return financialItemsMapper.selectOne(queryWrapper);
    }

    private List<OverdueRecord> getOverdueRecordByRiskControl(RiskControl riskControl) {
        LocalDateTime nowTime = LocalDateTime.now();
        Page<OverdueRecord> page = new Page<>(1, riskControl.getOverdueNumberLimit());//次数限制
        QueryWrapper<OverdueRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at")//未删除
                .ge("created_at", nowTime.plusYears(-riskControl.getOverdueYearLimit()))//创建时间大于 now-近期年份
                .and(wrapper -> wrapper.gt("overdue_amount", riskControl.getExceptionAmount())
                        .or().le("overdue_amount", riskControl.getExceptionAmount())
                        .and(wp1 -> wp1.ne("repayment_status", 0)
                                .or()
                                .eq("repayment_status", 0)
                                .ge("UNIX_TIMESTAMP(updated_at)-UNIX_TIMESTAMP(created_at)", riskControl.getExceptionDays() * 24 * 60 * 60)));//金额小于ExceptionAmount的例外
        /*
        SELECT * FROM overdue_record WHERE
         deleted_at IS NULL
          AND created_at > ?
          AND
          (overdue_amount >= ? OR overdue_amount < ? AND (repayment_status!= 0 OR repayment_status= 0 AND UNIX_TIMESTAMP(updated_at)-UNIX_TIMESTAMP(created_at)>= ? )) */
        overdueRecordMapper.selectPage(page, queryWrapper);
        return page.getRecords();
    }
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
        queryWrapper.isNull("deleted_at")
                .ge("start_time", nowTime)
                .le("start_time", nowTime.plusHours(2))
                .ge("end_time", nowTime);
        List<SeckillItems> seckillItemsList = seckillItemsMapper.selectList(queryWrapper);
        if (seckillItemsList != null && !seckillItemsList.isEmpty()) {
            for (SeckillItems item :
                    seckillItemsList) {
                Map<String, String> seckillItemMap = JSONUtils.toRedisHash(item);
                RedisUtils.hset("U:SeckillItem:" + item.getId(), seckillItemMap);
                Boolean res = RedisUtils.exists("U:SeckillItem:" + item.getId());//原先内容不存在
                if (res != null && !res) {
                    RedisUtils.hset("U:SeckillItem:" + item.getId(), seckillItemMap);
                }
                loadFinancial(item);//预热理财产品
                RiskControl riskControl = getRiskControlByID(item.getRiskControlId());//获取风控配置
                if (riskControl != null) {
                    loadPolicy(riskControl);//预热风控
                    loadDecisionResult(riskControl);//预热决策引擎分析结果
                }
            }
        }
    }

    /*
     * @MethodName loadDecisionResult
     * @author Wky1742095859
     * @Description 判断用户是否通过初筛
     * @Date 2022/4/17 16:36
     * @Param [item]
     * @Return void
     **/
    private void loadDecisionResult(RiskControl item) {
        int current = 1;
        while (true) {
            Page<User> page = new Page<>(current, 1000);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at");
            userMapper.selectPage(page, queryWrapper);
            List<User> itemsList = page.getRecords();
            if (itemsList == null || itemsList.isEmpty())
                break;
            current++;
            List<OverdueRecord> overdueRecordList;
            Map<String,Double> map=new HashMap<>();
            for (User user: itemsList) {
                //step 1 判断年龄 失信 就业状态
                if (IdcardUtil.getAgeByIdCard(user.getIdCard())<item.getAgeLimit()){
                    map.put(user.getPhone(),0.);
                    continue;
                }
                if (item.getWorkingStatusLimit()==1 && user.getEmploymentStatus()==0){
                    map.put(user.getPhone(),0.);
                    continue;
                }
                if (item.getUntrustworthyPersonLimit()==1 && user.getCreditStatus()==0){
                    map.put(user.getPhone(),0.);
                    continue;
                }
                overdueRecordList=getOverdueRecordByRiskControl(item);
                if (overdueRecordList==null ||
                        overdueRecordList.isEmpty() ||
                        overdueRecordList.size()<item.getOverdueNumberLimit()){
                    map.put(user.getPhone(),1.);
                    continue;
                }
                map.put(user.getPhone(),0.);
            }
            RedisUtils.zadd("U:RiskControlRes:" + item.getId(), map);
        }

    }

    private void loadFinancial(SeckillItems item) {
        FinancialItems financialItems = getFinancialItemByID(item.getFinancialItemId());
        if (financialItems != null) {
            Map<String, String> map = JSONUtils.toRedisHash(financialItems);
            RedisUtils.hset("U:FinancialItem:" + financialItems.getId(), map);
            /*
            Boolean res=RedisUtils.exists("U:FinancialItem:"+financialItems.getId());
            if (res==null || !res){
                Map<String,String> map=JSONUtils.toRedisHash(financialItems);
                RedisUtils.hset("U:FinancialItem:"+financialItems.getId(),map);
            }*/
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
    private void loadPolicy(RiskControl items) {
        Map<String, String> map = JSONUtils.toRedisHash(items);
        RedisUtils.hset("U:RiskControl:" + items.getId(), map);
    }

}
