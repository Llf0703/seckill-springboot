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
 * @since 2022-03-19
 */
@Data
@TableName("manager_user")
@ApiModel(value = "ManagerUsers对象", description = "")
public class ManagerUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private String account;

    private String password;

    private Integer productPermissions;

    private Integer seckillRecordPermissions;

    private Integer rechargeRecordPermissions;

    private Integer addAdminRights;

    @Override
    public String toString() {
        return "ManagerUsers{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deletedAt=" + deletedAt +
            ", account=" + account +
            ", password=" + password +
            ", productPermissions=" + productPermissions +
            ", seckillRecordPermissions=" + seckillRecordPermissions +
            ", rechargeRecordPermissions=" + rechargeRecordPermissions +
            ", addAdminRights=" + addAdminRights +
        "}";
    }
}
