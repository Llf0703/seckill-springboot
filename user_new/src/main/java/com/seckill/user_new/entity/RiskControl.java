package com.seckill.user_new.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
@Data
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
