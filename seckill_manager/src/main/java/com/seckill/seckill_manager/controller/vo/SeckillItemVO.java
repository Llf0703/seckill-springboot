package com.seckill.seckill_manager.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillItemVO {
    private Integer id;

    private String title;

    private Long stock;

    private BigDecimal amount;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    //private Long remainingStock;

    private Integer financialItemId;

    private Integer riskControlId;
}
