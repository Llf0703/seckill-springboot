package com.seckill.seckill.entity;

import lombok.*;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

@Data
@Builder(toBuilder = true)
@TableName("manager_user")
public class Manager {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date created_at, updated_at, deleted_at;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String account, password;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private int product_permissions, seckill_record_permissions, recharge_record_permissions, add_admin_rights;
}
