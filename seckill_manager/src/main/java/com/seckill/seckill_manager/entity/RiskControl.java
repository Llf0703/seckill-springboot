package com.seckill.seckill_manager.entity;

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
 * @since 2022-03-24
 */
@TableName("risk_control")
@ApiModel(value = "RiskControl对象", description = "")
public class RiskControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @ApiModelProperty("决策引擎名称")
    private String policyName;

    @ApiModelProperty("是否限制失业人员准入 0否 1是")
    private Integer workingStatusLimit;

    @ApiModelProperty("是否限制失信人员准入 0否 1是")
    private Integer untrustworthyPersonLimit;

    @ApiModelProperty("年龄限制 数据未最低限制年龄")
    private Integer ageLimit;

    @ApiModelProperty("最近overdue_year_limit年逾期overdue_number_limit次")
    private Integer overdueYearLimit;

    @ApiModelProperty("最近overdue_year_limit年逾期overdue_number_limit次")
    private Integer overdueNumberLimit;

    @ApiModelProperty("金额小于exception_amount,exception_days内还清的除外")
    private BigDecimal exceptionAmount;

    @ApiModelProperty("金额小于exception_amount,exception_days内还清的除外")
    private Integer exceptionDays;

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
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
    public Integer getWorkingStatusLimit() {
        return workingStatusLimit;
    }

    public void setWorkingStatusLimit(Integer workingStatusLimit) {
        this.workingStatusLimit = workingStatusLimit;
    }
    public Integer getUntrustworthyPersonLimit() {
        return untrustworthyPersonLimit;
    }

    public void setUntrustworthyPersonLimit(Integer untrustworthyPersonLimit) {
        this.untrustworthyPersonLimit = untrustworthyPersonLimit;
    }
    public Integer getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }
    public Integer getOverdueYearLimit() {
        return overdueYearLimit;
    }

    public void setOverdueYearLimit(Integer overdueYearLimit) {
        this.overdueYearLimit = overdueYearLimit;
    }
    public Integer getOverdueNumberLimit() {
        return overdueNumberLimit;
    }

    public void setOverdueNumberLimit(Integer overdueNumberLimit) {
        this.overdueNumberLimit = overdueNumberLimit;
    }
    public BigDecimal getExceptionAmount() {
        return exceptionAmount;
    }

    public void setExceptionAmount(BigDecimal exceptionAmount) {
        this.exceptionAmount = exceptionAmount;
    }
    public Integer getExceptionDays() {
        return exceptionDays;
    }

    public void setExceptionDays(Integer exceptionDays) {
        this.exceptionDays = exceptionDays;
    }

    @Override
    public String toString() {
        return "RiskControl{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deletedAt=" + deletedAt +
            ", policyName=" + policyName +
            ", workingStatusLimit=" + workingStatusLimit +
            ", untrustworthyPersonLimit=" + untrustworthyPersonLimit +
            ", ageLimit=" + ageLimit +
            ", overdueYearLimit=" + overdueYearLimit +
            ", overdueNumberLimit=" + overdueNumberLimit +
            ", exceptionAmount=" + exceptionAmount +
            ", exceptionDays=" + exceptionDays +
        "}";
    }
}
