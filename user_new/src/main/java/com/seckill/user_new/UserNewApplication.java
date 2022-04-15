package com.seckill.user_new;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.seckill.user_new.mapper")
@EnableTransactionManagement
public class UserNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserNewApplication.class, args);
	}

}
