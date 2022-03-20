package com.example.seckill_manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
@Data
@TableName("seckill_items")
@ApiModel(value = "SeckillItems对象", description = "")
public class SeckillItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private String title;

    private Long stock;

    private Long amount;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long remainingStock;

    private Integer financialItemId;


    @Override
    public String toString() {
        return "SeckillItems{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deletedAt=" + deletedAt +
            ", title=" + title +
            ", stock=" + stock +
            ", amount=" + amount +
            ", description=" + description +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", remainingStock=" + remainingStock +
            ", financialItemId=" + financialItemId +
        "}";
    }
}
