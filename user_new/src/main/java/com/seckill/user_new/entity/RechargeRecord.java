package com.seckill.user_new.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("recharge_record")
@ApiModel(value = "RechargeRecord对象", description = "")
public class RechargeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Integer userId;

    @ApiModelProperty("金额,小数点四位,十万不支持一次性充值")
    private BigDecimal amount;

    @ApiModelProperty("1微信 2支付宝 3云闪付 4其他")
    private Integer rechargeMethod;

    @ApiModelProperty("流水号")
    private Long serialNumber;

    private LocalDateTime rechargeTime;//充值时间
    @Override
    public String toString() {
        return "RechargeRecord{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", userId=" + userId +
                ", amount=" + amount +
                ", rechargeMethod=" + rechargeMethod +
                ", serialNumber=" + serialNumber +
                ", rechargeTime=" + rechargeTime +
                "}";
    }
}
