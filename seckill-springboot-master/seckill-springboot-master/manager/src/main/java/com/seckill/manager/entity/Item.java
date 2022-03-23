package com.seckill.manager.entity;

import lombok.*;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

@Data
@Builder(toBuilder = true)
@TableName("seckill_items")
public class Item {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date created_at, updated_at, deleted_at, start_time, end_time;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String title, description;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Long stock, amount, remaining_stock;
}
