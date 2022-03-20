package com.example.seckill_manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-20
 */
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Long getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Long remainingStock) {
        this.remainingStock = remainingStock;
    }
    public Integer getFinancialItemId() {
        return financialItemId;
    }

    public void setFinancialItemId(Integer financialItemId) {
        this.financialItemId = financialItemId;
    }

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
