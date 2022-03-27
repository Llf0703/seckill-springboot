package com.example.seckill_manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.seckill_manager.mapper")
public class SeckillManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillManagerApplication.class, args);
    }

}
