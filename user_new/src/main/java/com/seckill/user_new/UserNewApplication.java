package com.seckill.user_new;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.seckill.user_new.mapper")
@EnableTransactionManagement
@EnableScheduling
public class UserNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserNewApplication.class, args);
	}

}
