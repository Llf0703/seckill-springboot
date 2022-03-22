package com.seckill.seckill_manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wky1742095859
 * @since 2022-03-23
 */
@Data
@TableName("manager_user")
@ApiModel(value = "ManagerUser对象", description = "")
public class ManagerUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private String account;

    private String password;

    @ApiModelProperty("秒杀活动权限 0无权限 1可读 2可编辑")
    private Integer seckillItemsPermissions;

    @ApiModelProperty("秒杀记录权限 0无权限 1可读")
    private Integer seckillRecordPermissions;

    @ApiModelProperty("充值记录权限 0无权限 1可读")
    private Integer rechargeRecordPermissions;

    @ApiModelProperty("添加管理员权限 0无权限 1可编辑")
    private Integer addAdminRights;

    @ApiModelProperty("理财产品权限 0无权限 1可读 2可编辑")
    private Integer financialItemsPermissions;

    @ApiModelProperty("风险引擎权限 0无权限 1可读 2可编辑")
    private Integer riskControlPermissions;

    @ApiModelProperty("客户信息权限 0无权限 1可读 2可编辑")
    private Integer guestInfoPermissions;


    @Override
    public String toString() {
        return "ManagerUser{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", account=" + account +
                ", password=" + password +
                ", seckillItemsPermissions=" + seckillItemsPermissions +
                ", seckillRecordPermissions=" + seckillRecordPermissions +
                ", rechargeRecordPermissions=" + rechargeRecordPermissions +
                ", addAdminRights=" + addAdminRights +
                ", financialItemsPermissions=" + financialItemsPermissions +
                ", riskControlPermissions=" + riskControlPermissions +
                ", guestInfoPermissions=" + guestInfoPermissions +
                "}";
    }
}
