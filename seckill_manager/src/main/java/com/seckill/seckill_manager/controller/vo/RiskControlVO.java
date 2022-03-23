package com.seckill.seckill_manager.controller.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskControlVO {
    private Integer id;

    private String policyName;

    private Integer workingStatusLimit;

    private Integer untrustworthyPersonLimit;

    private Integer ageLimit;

    private Integer overdueYearLimit;

    private Integer overdueNumberLimit;

    private BigDecimal exceptionAmount;

    private Integer exceptionDays;
}
