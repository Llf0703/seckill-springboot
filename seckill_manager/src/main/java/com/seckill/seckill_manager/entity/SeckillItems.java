package com.seckill.seckill_manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@TableName("seckill_items")
public class SeckillItems {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date created_at, updated_at, deleted_at, start_time, end_time;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String title, description;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Long stock, amount, remaining_stock;
}

