package com.seckill.user_new.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Integer id;

    private String userName;

    private String name;

    private String password;

    private String email;

    private String phone;

    private String idCard;

    private BigDecimal balance;

    private LocalDateTime age;

    private Integer employmentStatus;

    private Integer creditStatus;

    private String uid;

    private String token;

    private String captcha;
}
