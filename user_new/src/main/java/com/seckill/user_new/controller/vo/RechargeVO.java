package com.seckill.user_new.controller.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeVO {
    private BigDecimal amount;
    private Integer rechargeMethod;
}
