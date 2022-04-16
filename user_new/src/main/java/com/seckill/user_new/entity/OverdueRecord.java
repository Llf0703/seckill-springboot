package com.seckill.user_new.entity;

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
 * @since 2022-04-17
 */
@TableName("overdue_record")
@ApiModel(value = "OverdueRecord对象", description = "")
public class OverdueRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Integer userId;

    @ApiModelProperty("逾期金额")
    private BigDecimal overdueAmount;

    @ApiModelProperty("剩余待还金额")
    private BigDecimal repaymentStatus;

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
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }
    public BigDecimal getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(BigDecimal repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    @Override
    public String toString() {
        return "OverdueRecord{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deletedAt=" + deletedAt +
            ", userId=" + userId +
            ", overdueAmount=" + overdueAmount +
            ", repaymentStatus=" + repaymentStatus +
        "}";
    }
}
