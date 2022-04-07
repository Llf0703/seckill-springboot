package com.seckill.user_new.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
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

    private BigDecimal interestRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getDailyCumulativeLimit() {
        return dailyCumulativeLimit;
    }

    public void setDailyCumulativeLimit(BigDecimal dailyCumulativeLimit) {
        this.dailyCumulativeLimit = dailyCumulativeLimit;
    }
    public Integer getAutomaticRedemptionAtMaturity() {
        return automaticRedemptionAtMaturity;
    }

    public void setAutomaticRedemptionAtMaturity(Integer automaticRedemptionAtMaturity) {
        this.automaticRedemptionAtMaturity = automaticRedemptionAtMaturity;
    }
    public BigDecimal getIncrementAmount() {
        return incrementAmount;
    }

    public void setIncrementAmount(BigDecimal incrementAmount) {
        this.incrementAmount = incrementAmount;
    }
    public LocalDateTime getProductExpirationDate() {
        return productExpirationDate;
    }

    public void setProductExpirationDate(LocalDateTime productExpirationDate) {
        this.productExpirationDate = productExpirationDate;
    }
    public BigDecimal getMaximumSinglePurchaseAmount() {
        return maximumSinglePurchaseAmount;
    }

    public void setMaximumSinglePurchaseAmount(BigDecimal maximumSinglePurchaseAmount) {
        this.maximumSinglePurchaseAmount = maximumSinglePurchaseAmount;
    }
    public BigDecimal getMinimumDepositAmount() {
        return minimumDepositAmount;
    }

    public void setMinimumDepositAmount(BigDecimal minimumDepositAmount) {
        this.minimumDepositAmount = minimumDepositAmount;
    }
    public Integer getEarlyWithdrawal() {
        return earlyWithdrawal;
    }

    public void setEarlyWithdrawal(Integer earlyWithdrawal) {
        this.earlyWithdrawal = earlyWithdrawal;
    }
    public LocalDateTime getProductEffectiveDate() {
        return productEffectiveDate;
    }

    public void setProductEffectiveDate(LocalDateTime productEffectiveDate) {
        this.productEffectiveDate = productEffectiveDate;
    }
    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

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
