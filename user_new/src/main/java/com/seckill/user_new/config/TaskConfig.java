package com.seckill.user_new.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
public class TaskConfig {
    private final int corePoolSize = 10;
    private final int maxPoolSize = 200;
    private final int queueCapacity = 10;
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskExecutor = new ThreadPoolTaskScheduler();
        taskExecutor.setPoolSize(50);
        return taskExecutor;
    }
}
