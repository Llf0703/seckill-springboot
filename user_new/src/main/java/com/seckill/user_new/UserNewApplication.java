package com.seckill.user_new;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seckill.user_new.mapper")
public class UserNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserNewApplication.class, args);
	}

}
