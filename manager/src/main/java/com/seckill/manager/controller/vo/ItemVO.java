package com.seckill.manager.controller.vo;

import lombok.Data;

@Data
public class ItemVO {
    private String title, description, start_time, end_time;
    private Long amount, stock;
}
