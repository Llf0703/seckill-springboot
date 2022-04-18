package com.seckill.user_new.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.user_new.common.Response;
import com.seckill.user_new.entity.RedisService.SeckillRecordRedis;
import com.seckill.user_new.entity.SeckillItems;
import com.seckill.user_new.entity.SeckillRecord;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.mapper.SeckillItemsMapper;
import com.seckill.user_new.mapper.SeckillRecordMapper;
import com.seckill.user_new.mapper.UserMapper;
import com.seckill.user_new.service.impl.SeckillItemsServiceImpl;
import com.seckill.user_new.service.impl.UserServiceImpl;
import com.seckill.user_new.utils.JSONUtils;
import com.seckill.user_new.utils.RedisUtils;
import com.seckill.user_new.utils.Snowflake;
import com.seckill.user_new.utils.UUIDUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import javax.annotation.Resource;
import javax.swing.text.Style;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Transactional
public class DoSeckillTask extends ServiceImpl<SeckillRecordMapper, SeckillRecord> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private SeckillItemsMapper seckillItemsMapper;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private SeckillItemsServiceImpl seckillItemsService;
    private String lastStrID = null;
    private String lastDBErrorID = null;

    private User getUserByID(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return userMapper.selectOne(queryWrapper);
    }
    private SeckillItems getSeckillItemByID(Integer id) {
        QueryWrapper<SeckillItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return seckillItemsMapper.selectOne(queryWrapper);
    }
    /*
    @Scheduled(fixedDelay = 10)
    public void test() throws Exception{
        Boolean res = RedisUtils.exists("U:SeckillItem:" + 2);
        if (!Boolean.FALSE.equals(res)) {
            List<String> timeStr = RedisUtils.hmget("U:SeckillItem:" + 2, "startTime", "endTime", "amount");
            if (timeStr == null || timeStr.size() != 3) {
                System.out.println("系统异常");
                throw new Exception();
            }
            LocalDateTime startTime = LocalDateTime.parse(timeStr.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(timeStr.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime nowTime = LocalDateTime.now();
            if (startTime.isAfter(nowTime)){
                System.out.println("未开始");
                throw new Exception();
            }
            if (endTime.isBefore(nowTime)){
                System.out.println("已结束");
                throw new Exception();
            }
            SeckillRecordRedis seckillRecordRedis = new SeckillRecordRedis(2,
                    "15878295798", LocalDateTime.now(), new BigDecimal(timeStr.get(2)), 0,
                    2, Snowflake.nextLongID());
            String recordStr = JSONUtils.toJSONStr(seckillRecordRedis);
            String uid = UUIDUtil.getUUID();
            String res1 = RedisUtils.set("U:DoSeckill:" + uid, recordStr, 60);
            if (!Objects.equals(res1, "OK")){
                System.out.println("秒杀失败,系统异常");
                throw new Exception();
            }
            Response response=seckillItemsService.doSeckillTest(uid);
            System.out.println(response);
        }
    }
    @Scheduled(fixedDelay = 10)
    public void test2() throws Exception{
        Boolean res = RedisUtils.exists("U:SeckillItem:" + 2);
        if (!Boolean.FALSE.equals(res)) {
            List<String> timeStr = RedisUtils.hmget("U:SeckillItem:" + 2, "startTime", "endTime", "amount");
            if (timeStr == null || timeStr.size() != 3) {
                System.out.println("系统异常");
                throw new Exception();
            }
            LocalDateTime startTime = LocalDateTime.parse(timeStr.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(timeStr.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime nowTime = LocalDateTime.now();
            if (startTime.isAfter(nowTime)){
                System.out.println("未开始");
                throw new Exception();
            }
            if (endTime.isBefore(nowTime)){
                System.out.println("已结束");
                throw new Exception();
            }
            SeckillRecordRedis seckillRecordRedis = new SeckillRecordRedis(2,
                    "15878295798", LocalDateTime.now(), new BigDecimal(timeStr.get(2)), 0,
                    2, Snowflake.nextLongID());
            String recordStr = JSONUtils.toJSONStr(seckillRecordRedis);
            String uid = UUIDUtil.getUUID();
            String res1 = RedisUtils.set("U:DoSeckill:" + uid, recordStr, 60);
            if (!Objects.equals(res1, "OK")){
                System.out.println("秒杀失败,系统异常");
                throw new Exception();
            }
            Response response=seckillItemsService.doSeckillTest(uid);
            System.out.println(response);
        }
    }
    @Scheduled(fixedDelay = 10)
    public void test3() throws Exception{
        Boolean res = RedisUtils.exists("U:SeckillItem:" + 2);
        if (!Boolean.FALSE.equals(res)) {
            List<String> timeStr = RedisUtils.hmget("U:SeckillItem:" + 2, "startTime", "endTime", "amount");
            if (timeStr == null || timeStr.size() != 3) {
                System.out.println("系统异常");
                throw new Exception();
            }
            LocalDateTime startTime = LocalDateTime.parse(timeStr.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(timeStr.get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime nowTime = LocalDateTime.now();
            if (startTime.isAfter(nowTime)){
                System.out.println("未开始");
                throw new Exception();
            }
            if (endTime.isBefore(nowTime)){
                System.out.println("已结束");
                throw new Exception();
            }
            SeckillRecordRedis seckillRecordRedis = new SeckillRecordRedis(2,
                    "15878295798", LocalDateTime.now(), new BigDecimal(timeStr.get(2)), 0,
                    2, Snowflake.nextLongID());
            String recordStr = JSONUtils.toJSONStr(seckillRecordRedis);
            String uid = UUIDUtil.getUUID();
            String res1 = RedisUtils.set("U:DoSeckill:" + uid, recordStr, 60);
            if (!Objects.equals(res1, "OK")){
                System.out.println("秒杀失败,系统异常");
                throw new Exception();
            }
            Response response=seckillItemsService.doSeckillTest(uid);
            System.out.println(response);
        }
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
                //刷新用户数据 扣余额 刷新库存 扣库存
                List<User> userList = new LinkedList<>();
                List<SeckillItems> seckillItemsList=new LinkedList<>();
                SeckillItems seckillItems;
                User user;
                for (SeckillRecord record : seckillRecordList) {
                    if (record.getStatus() == 0)
                        continue;
                    user = getUserByID(record.getUserId());
                    seckillItems=getSeckillItemByID(record.getSeckillItemsId());
                    if (user == null || seckillItems==null)
                        continue;
                    LocalDateTime now=LocalDateTime.now();
                    Long stock=seckillItems.getRemainingStock();
                    BigDecimal balance = user.getBalance();
                    seckillItems.setRemainingStock(stock-1);
                    seckillItems.setUpdatedAt(now);
                    user.setBalance(balance.subtract(record.getAmount()));
                    user.setUpdatedAt(now);
                    userList.add(user);
                    seckillItemsList.add(seckillItems);
                }
                if (!userList.isEmpty() && !seckillItemsList.isEmpty()){
                    boolean res=userService.updateBatchById(userList);
                    boolean res1=seckillItemsService.updateBatchById(seckillItemsList);
                    if (!res || !res1)
                        throw new RuntimeException();//进行回滚
                }
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
                //刷新用户数据 扣余额 刷新库存 扣库存
                List<User> userList = new LinkedList<>();
                List<SeckillItems> seckillItemsList=new LinkedList<>();
                SeckillItems seckillItems;
                User user;
                for (SeckillRecord record : seckillRecordList) {
                    if (record.getStatus() == 0)
                        continue;
                    user = getUserByID(record.getUserId());
                    seckillItems=getSeckillItemByID(record.getSeckillItemsId());
                    if (user == null)
                        continue;
                    if (seckillItems==null)
                        continue;
                    LocalDateTime now=LocalDateTime.now();
                    Long stock=seckillItems.getRemainingStock();
                    BigDecimal balance = user.getBalance();
                    seckillItems.setRemainingStock(stock-1);
                    seckillItems.setUpdatedAt(now);
                    user.setBalance(balance.subtract(record.getAmount()));
                    user.setUpdatedAt(now);
                    userList.add(user);
                    seckillItemsList.add(seckillItems);
                }
                boolean res=userService.updateBatchById(userList);
                boolean res1=seckillItemsService.updateBatchById(seckillItemsList);
                if (!res || !res1)
                    throw new RuntimeException();//进行回滚
                StreamEntryID lastID = streamEntryList.get(streamEntryList.size() - 1).getID();
                lastDBErrorID = lastID.getTime() + "-" + (lastID.getSequence() + 1);
                RedisUtils.set("U:SeckillMessageQueue:lastDBErrorID", lastDBErrorID);
            }
        }
    }
}
