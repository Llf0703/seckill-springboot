package com.seckill.user_new.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.entity.SeckillRecord;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.SeckillRecordMapper;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.service.impl.UserServiceImpl;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class DoSeckillTask extends ServiceImpl<SeckillRecordMapper, SeckillRecord> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserServiceImpl userService;

    private String lastStrID = null;
    private String lastDBErrorID = null;

    private User getUserByID(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return userMapper.selectOne(queryWrapper);
    }
    /*
     * @MethodName doRecharge
     * @author Wky1742095859
     * @Description 秒杀消息队列消费
     * @Date 2022/4/17 17:58
     * @Param []
     * @Return void
     **/
    @Scheduled(fixedDelay = 1000)
    public void doSeckill() throws Exception {
        while (true) {
            String id = RedisUtils.get("U:SeckillMessageQueue:lastID");
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
            List<StreamEntry> streamEntryList = RedisUtils.xrange("U:SeckillMessageQueue:queue", startID, null, 1000);
            if (streamEntryList == null || streamEntryList.isEmpty()) {
                break;
            }
            List<SeckillRecord> seckillRecordList = new LinkedList<>();
            for (StreamEntry msg :
                    streamEntryList) {
                Map<String, String> msgText = msg.getFields();
                SeckillRecord msgEntity = JSONUtils.toEntity(msgText, SeckillRecord.class);
                if (msgEntity != null) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    msgEntity.setCreatedAt(nowTime);
                    msgEntity.setUpdatedAt(nowTime);
                    seckillRecordList.add(msgEntity);
                } else {
                    RedisUtils.xadd("U:SeckillMessageQueue:DBErrorQueue", msgText, 100000, false);//加入mysql更新错误处理队列
                }
            }
            if (saveBatch(seckillRecordList)) {
                //刷新用户数据 扣余额
                List<User> userList = new LinkedList<>();
                User user;
                for (SeckillRecord record : seckillRecordList) {
                    if (record.getStatus() == 0)
                        continue;
                    user = getUserByID(record.getUserId());
                    if (user == null)
                        continue;
                    BigDecimal balance = user.getBalance();
                    user.setBalance(balance.subtract(record.getAmount()));
                    user.setUpdatedAt(LocalDateTime.now());
                    userList.add(user);
                }
                boolean res=userService.updateBatchById(userList);
                if (!res)
                    throw new RuntimeException();//进行回滚
                StreamEntryID lastID = streamEntryList.get(streamEntryList.size() - 1).getID();
                lastStrID = lastID.getTime() + "-" + (lastID.getSequence() + 1);
                RedisUtils.set("U:SeckillMessageQueue:lastID", lastStrID);
            }
        }

    }

    /*
     * @MethodName HandleRedisError
     * @author Wky1742095859
     * @Description 处理db异常
     * @Date 2022/4/17 18:35
     * @Param []
     * @Return void
     **/
    @Scheduled(fixedDelay = 1000)
    public void HandleRedisError() throws Exception {
        //第二轮重试, 再次失败则抛弃本次秒杀
        String id = RedisUtils.get("U:SeckillMessageQueue:lastDBErrorID");
        String startID;
        if (id == null) {
            startID = lastDBErrorID;
        } else {
            StreamEntryID redisID = new StreamEntryID(id);
            if (lastDBErrorID == null || redisID.compareTo(new StreamEntryID(lastDBErrorID)) > 0) {
                startID = redisID.toString();
            } else {
                startID = lastDBErrorID;
            }
        }
        List<StreamEntry> streamEntryList = RedisUtils.xrange("U:SeckillMessageQueue:DBErrorQueue", startID, null, 1000);
        if (streamEntryList != null && !streamEntryList.isEmpty()) {
            List<SeckillRecord> seckillRecordList = new LinkedList<>();
            for (StreamEntry msg :
                    streamEntryList) {
                Map<String, String> msgText = msg.getFields();
                SeckillRecord msgEntity = JSONUtils.toEntity(msgText, SeckillRecord.class);
                if (msgEntity != null) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    msgEntity.setCreatedAt(nowTime);
                    msgEntity.setUpdatedAt(nowTime);
                    seckillRecordList.add(msgEntity);
                }
            }
            if (saveBatch(seckillRecordList)) {
                //刷新用户数据 扣余额
                List<User> userList = new LinkedList<>();
                User user;
                for (SeckillRecord record : seckillRecordList) {
                    if (record.getStatus() == 0)
                        continue;
                    user = getUserByID(record.getUserId());
                    if (user == null)
                        continue;
                    BigDecimal balance = user.getBalance();
                    user.setBalance(balance.subtract(record.getAmount()));
                    user.setUpdatedAt(LocalDateTime.now());
                    userList.add(user);
                }
                boolean res=userService.updateBatchById(userList);
                if (!res)
                    throw new RuntimeException();//进行回滚
                StreamEntryID lastID = streamEntryList.get(streamEntryList.size() - 1).getID();
                lastDBErrorID = lastID.getTime() + "-" + (lastID.getSequence() + 1);
                RedisUtils.set("U:SeckillMessageQueue:lastDBErrorID", lastDBErrorID);
            }
        }
    }
}
