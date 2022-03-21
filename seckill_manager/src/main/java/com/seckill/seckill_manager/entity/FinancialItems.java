package com.seckill.seckill_manager.entity;

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
 * @since 2022-03-20
 */
@Data
@TableName("financial_items")
@ApiModel(value = "FinancialItems对象", description = "")
public class FinancialItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

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


    @Override
    public String toString() {
        return "FinancialItems{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", productName=" + productName +
                ", dailyCumulativeLimit=" + dailyCumulativeLimit +
                ", automaticRedemptionAtMaturity=" + automaticRedemptionAtMaturity +
                ", incrementAmount=" + incrementAmount +
                ", productExpirationDate=" + productExpirationDate +
                ", maximumSinglePurchaseAmount=" + maximumSinglePurchaseAmount +
                ", minimumDepositAmount=" + minimumDepositAmount +
                ", earlyWithdrawal=" + earlyWithdrawal +
                ", productEffectiveDate=" + productEffectiveDate +
                ", shelfLife=" + shelfLife +
                ", interestRate=" + interestRate +
                "}";
    }
}
