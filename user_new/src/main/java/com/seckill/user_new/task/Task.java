package com.seckill.user_new.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.seckill.user_new.entity.RedisService.LoginUser;
import com.seckill.user_new.entity.User;
import com.seckill.user_new.utils.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.StreamEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Task
 * @description:  定时任务
 * @author Wky1742095859
 * @date 2022/4/12 23:20
 * @version 1.0
 */
@Component
@EnableScheduling
@Async
public class Task {
    /*
     * @MethodName doRecharge
     * @author Wky1742095859
     * @Description 充值消息队列消费
     * @Date 2022/4/12 23:20
     * @Param []
     * @Return void
    **/
    //@Scheduled(fixedDelay = 5000)
    public void doRecharge() throws Exception {
        System.out.println("producut" + new Date());
        Map<String,String> msg=new HashMap<>();
        msg.put("test",new Date().toString());
        String res=RedisUtils.xadd("test",msg);
        System.out.println(res);
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
        User test=new User();
        test.setBalance(new BigDecimal("4546522"));
        test.setAge(LocalDateTime.now());
        test.setCreditStatus(1);
        Map<String,String> map= JSON.parseObject(JSON.toJSONString(test),new TypeReference<Map<String,String>>(){});
        System.out.println(map);
        System.out.println(RedisUtils.hset("testhash",map));
        System.out.println(RedisUtils.hmget("testhash","balance","age"));
        System.out.println(RedisUtils.hget("tqweqw","eqwewe"));
        /*
        System.out.println("c" + new Date());
        List<Map.Entry<String, List<StreamEntry>>> res= RedisUtils.xreadall("test1");
        if (res!=null && !res.isEmpty()) {
            Map.Entry<String, List<StreamEntry>> msg = res.get(0);
            //System.out.println(msg);
            //System.out.println(msg.getKey()); //队列名
            List<StreamEntry> entrylist = msg.getValue();
            System.out.println(entrylist);
            System.out.println(entrylist.size());
            String id = entrylist.get(0).getID().toString();
            Map<String,String> test= entrylist.get(1).getFields();
        }*/
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
    public void loadCache() throws Exception{

    }
}
