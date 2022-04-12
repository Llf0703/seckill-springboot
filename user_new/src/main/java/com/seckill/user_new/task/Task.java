package com.seckill.user_new.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    @Scheduled(fixedDelay = 5000)
    public void doRecharge() throws Exception {
        System.out.println("test" + new Date());
    }
    /*
     * @MethodName doSeckill
     * @author Wky1742095859
     * @Description 秒杀队列消费
     * @Date 2022/4/12 23:21
     * @Param []
     * @Return void
    **/
    @Scheduled(fixedDelay = 5000)
    public void doSeckill() throws Exception {
        System.out.println("test1" + new Date());
        throw new Exception();
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
