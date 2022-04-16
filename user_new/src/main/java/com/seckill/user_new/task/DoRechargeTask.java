package com.seckill.user_new.task;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.entity.RechargeRecord;
import com.seckill.user_new.entity.RedisService.DoRecharge;
import com.seckill.user_new.mapper.RechargeRecordMapper;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

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
    private String lastRedisErrorID = null;

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
        String id = RedisUtils.get("U:RechargeMessageQueue:lastID");
        String startID;
        if (id == null) {
            startID = lastStrID;
        } else {
            StreamEntryID redisID = new StreamEntryID(id);
            if (lastStrID == null || redisID.compareTo(new StreamEntryID(lastStrID)) > 0) {
                startID = redisID.toString();
            } else {
                startID = lastStrID;
            }
        }
        List<StreamEntry> streamEntryList = RedisUtils.xrange("U:RechargeMessageQueue:queue", startID, null, 1000);
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
                } else {
                    RedisUtils.xadd("U:RechargeMessageQueue:DBErrorQueue", msgText, 100000, false);//加入mysql更新错误处理队列
                }
            }
            if (saveBatch(rechargeRecordList)) {
                for (StreamEntry msg :
                        streamEntryList) {
                    Map<String, String> msgText = msg.getFields();
                    DoRecharge msgEntity = JSONUtils.toEntity(msgText, DoRecharge.class);
                    assert msgEntity != null;
                    Double res = RedisUtils.hincrbyfloat("U:User:" + msgEntity.getPhone(), "balance", msgEntity.getAmount().floatValue());
                    if (res == null) {
                        RedisUtils.xadd("U:RechargeMessageQueue:RedisErrorQueue", msgText, 100000, false);//加入redis更新错误队列
                    }
                }
                StreamEntryID lastID = streamEntryList.get(streamEntryList.size() - 1).getID();
                lastStrID = lastID.getTime() + "-" + (lastID.getSequence() + 1);
                RedisUtils.set("U:RechargeMessageQueue:lastID", lastStrID);
            }
        }
    }
    /*
     * @MethodName doSeckill
     * @author Wky1742095859
     * @Description 处理redis异常
     * @Date 2022/4/12 23:21
     * @Param []
     * @Return void
     **/
    @Scheduled(fixedDelay = 1000)
    public void HandleRedisError() throws Exception {
        String id = RedisUtils.get("U:RechargeMessageQueue:lastRedisErrorID");
        String startID;
        if (id == null) {
            startID = lastRedisErrorID;
        } else {
            StreamEntryID redisID = new StreamEntryID(id);
            if (lastRedisErrorID == null || redisID.compareTo(new StreamEntryID(lastRedisErrorID)) > 0) {
                startID = redisID.toString();
            } else {
                startID = lastRedisErrorID;
            }
        }
        List<StreamEntry> streamEntryList = RedisUtils.xrange("U:RechargeMessageQueue:RedisErrorQueue", startID, null, 1000);
        if (streamEntryList != null && !streamEntryList.isEmpty()) {
            for (StreamEntry msg :
                    streamEntryList) {
                Map<String, String> msgText = msg.getFields();
                DoRecharge msgEntity = JSONUtils.toEntity(msgText, DoRecharge.class);
                if (msgEntity != null) {
                    Double res = RedisUtils.hincrbyfloat("U:User:" + msgEntity.getPhone(), "balance", msgEntity.getAmount().floatValue());
                    if (res == null) {
                        RedisUtils.xadd("U:RechargeMessageQueue:RedisErrorQueue", msgText, 100000, false);//加入redis更新错误队列
                    }
                } else {
                    RedisUtils.xadd("U:RechargeMessageQueue:RedisErrorQueue", msgText, 100000, false);//加入重新排入redis更新错误处理队列
                }
            }
            StreamEntryID lastID = streamEntryList.get(streamEntryList.size() - 1).getID();
            lastRedisErrorID = lastID.getTime() + "-" + (lastID.getSequence() + 1);
            RedisUtils.set("U:RechargeMessageQueue:lastRedisErrorID", lastRedisErrorID);
        }
    }

}
