package com.seckill.user_new.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-08
 */
@Data
@TableName("seckill_record")
@ApiModel(value = "ExpensesRecord对象", description = "")
public class ExpensesRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Integer userId;

    private BigDecimal amount;

    private String reason;

    private Integer status;

    private Integer seckillItemsId;

    @Override
    public String toString() {
        return "ExpensesRecord{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", userId=" + userId +
                ", amount=" + amount +
                ", seckillItemsId=" + seckillItemsId +
                "}";
    }
}
