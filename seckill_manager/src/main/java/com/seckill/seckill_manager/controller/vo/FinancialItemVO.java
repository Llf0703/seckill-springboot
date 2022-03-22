package com.seckill.seckill_manager.controller.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinancialItemVO {
    private Integer id;

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

    private Float interestRate;
}
