package com.seckill.user_new.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seckill.user_new.entity.OverdueRecord;
import com.seckill.user_new.entity.RiskControl;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.OverdueRecordMapper;
import com.seckill.user_new.mapper.RiskControlMapper;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private OverdueRecordMapper overdueRecordMapper;
    @Resource
    private UserMapper userMapper;

    private RiskControl getRiskControlByID(Integer id) {
        QueryWrapper<RiskControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return riskControlMapper.selectOne(queryWrapper);
    }

    private List<OverdueRecord> getOverdueRecordByRiskControl(RiskControl riskControl) {
        LocalDateTime nowTime = LocalDateTime.now();
        Page<SeckillItems> page = new Page<>(1, riskControl.getOverdueNumberLimit());//次数限制
        QueryWrapper<OverdueRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at")//未删除
                .ge("created_at", nowTime.plusYears(-riskControl.getOverdueYearLimit()))//创建时间大于 now-近期年份
                .and(wrapper -> wrapper.ge("overdue_amount", riskControl.getExceptionAmount())
                        .or().lt("overdue_amount", riskControl.getExceptionAmount())
                        .and(wp1 -> wp1.ne("repayment_status", 0)
                                .or()
                                .eq("repayment_status", 0)
                                .ge("UNIX_TIMESTAMP(updated_at)-UNIX_TIMESTAMP(created_at)", riskControl.getExceptionDays() * 24 * 60*60)));//金额小于ExceptionAmount的例外
        /*
        SELECT * FROM overdue_record WHERE
         deleted_at IS NULL
          AND created_at > ?
          AND (overdue_amount >= ? OR overdue_amount < ? AND (repayment_status!= 0 OR repayment_status= 0 AND UNIX_TIMESTAMP(updated_at)-UNIX_TIMESTAMP(created_at)>= ? )) */
        return overdueRecordMapper.selectList(queryWrapper);
    }

    @Scheduled(fixedDelay = 2000)
    public void loadItem1() throws Exception {
        RiskControl test1=new RiskControl();
        test1.setExceptionAmount(new BigDecimal("2000"));
        test1.setExceptionDays(5);
        test1.setOverdueNumberLimit(3);
        test1.setOverdueYearLimit(2);
        List<OverdueRecord> test=getOverdueRecordByRiskControl(test1);
        System.out.println(test);
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
                Boolean res = RedisUtils.exists("U:SeckillItem:" + item.getId());//原先内容不存在
                if (res != null && !res) {
                    RedisUtils.hset("U:SeckillItem:" + item.getId(), seckillItemMap);
                }
                loadPolicy(item);//预热风控
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
        RiskControl riskControl = getRiskControlByID(items.getId());//获取风控配置
        if (riskControl!=null) {
            while (true) {
                int current = 1;
                Page<User> page = new Page<>(current, 1000);
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.isNull("deleted_at");
                userMapper.selectPage(page, queryWrapper);
                List<User> itemsList = page.getRecords();
                if (itemsList == null || itemsList.isEmpty())
                    break;

            }
        }
    }

    /*
     * @MethodName isValidUser
     * @author Wky1742095859
     * @Description 判断用户是否通过初筛
     * @Date 2022/4/17 2:27
     * @Param [user, riskControl]
     * @Return java.util.Map<java.lang.String,java.lang.Double>
     **/
    private Map<String, Double> isValidUser(User user, RiskControl riskControl) {
        return null;
    }
}
