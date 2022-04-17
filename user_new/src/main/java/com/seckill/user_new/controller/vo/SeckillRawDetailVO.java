package com.seckill.user_new.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillRawDetailVO {
    private Integer id;

    private String title;

    private Long stock;

    private BigDecimal amount;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long remainingStock;
    //理财产品部分
    private String productName;

    private BigDecimal dailyCumulativeLimit;

    private Integer automaticRedemptionAtMaturity;

    private BigDecimal incrementAmount;

    private LocalDateTime productExpirationDate;

    private BigDecimal maximumSinglePurchaseAmount;

    private BigDecimal minimumDepositAmount;

    private Integer earlyWithdrawal;

    private LocalDateTime productEffectiveDate;

    private Integer shelfLife;

    private BigDecimal interestRate;

    //风控部分
    private Integer workingStatusLimit;

    private Integer untrustworthyPersonLimit;

    private Integer ageLimit;

    private Integer overdueYearLimit;

    private Integer overdueNumberLimit;

    private BigDecimal exceptionAmount;

    private Integer exceptionDays;
}
