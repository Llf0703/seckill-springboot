package com.seckill.user_new.task;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.entity.RechargeRecord;
import com.seckill.user_new.entity.RedisService.DoRecharge;
import com.seckill.user_new.mapper.RechargeRecordMapper;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import com.seckill.user_new.utils.Snowflake;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName Task
 * @description: 定时任务
 * @date 2022/4/12 23:20
 */
@Component
@EnableScheduling
@Async
@Transactional
public class DoRechargeTask extends ServiceImpl<RechargeRecordMapper, RechargeRecord> {
    private String lastStrID = null;
    /*
     * @MethodName doRecharge
     * @author Wky1742095859
     * @Description 充值消息队列消费
     * @Date 2022/4/12 23:20
     * @Param []
     * @Return void
     **/
    @Scheduled(fixedDelay = 1000)
    public void doRecharge() throws Exception {
        List<StreamEntry> streamEntryList = RedisUtils.xrange("U:RechargeMessageQueue:", lastStrID, null, 1000);
        if (streamEntryList != null && !streamEntryList.isEmpty()) {
            List<RechargeRecord> rechargeRecordList = new LinkedList<>();
            for (StreamEntry msg :
                    streamEntryList) {
                Map<String, String> msgText = msg.getFields();
                RechargeRecord msgEntity = JSONUtils.toEntity(msgText, RechargeRecord.class);
                if (msgEntity != null) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    msgEntity.setCreatedAt(nowTime);
                    msgEntity.setUpdatedAt(nowTime);
                    rechargeRecordList.add(msgEntity);
                }
            }
            if (saveBatch(rechargeRecordList)) {
                StreamEntryID lastID = streamEntryList.get(streamEntryList.size() - 1).getID();
                lastStrID = lastID.getTime() + "-" + (lastID.getSequence() + 1);
            }
        }
    }
    /*
     * @MethodName doSeckill
     * @author Wky1742095859
     * @Description 秒杀队列消费
     * @Date 2022/4/12 23:21
     * @Param []
     * @Return void
     **/

    @Scheduled(fixedDelay = 3000)
    public void doSeckill() throws Exception {
        /*
        User test=new User();
        test.setBalance(new BigDecimal("4546522"));
        test.setAge(LocalDateTime.now());
        test.setCreditStatus(1);
        Map<String,String> map= JSON.parseObject(JSON.toJSONString(test),new TypeReference<Map<String,String>>(){});
        System.out.println(map);
        System.out.println(RedisUtils.hset("testhash",map));
        System.out.println(RedisUtils.hmget("testhash","balance","age"));
        System.out.println(RedisUtils.hget("tqweqw","eqwewe"));*/
        /*
        System.out.println("c" + new Date());
        List<Map.Entry<String, List<StreamEntry>>> streamList = RedisUtils.xread(2, "test");
        System.out.println(streamList);
        if (streamList != null && !streamList.isEmpty()) {
            Map.Entry<String, List<StreamEntry>> stream = streamList.get(0);
            //System.out.println(msg);
            //System.out.println(msg.getKey()); //队列名
            List<StreamEntry> msglist = stream.getValue();
            System.out.println(msglist);
            System.out.println(msglist.size());
            String id = msglist.get(0).getID().toString();
            Map<String,String> test= msglist.get(0).getFields();
        }
        System.out.println("------------------------------------------");*/

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
    public void loadCache() throws Exception {

    }
}
