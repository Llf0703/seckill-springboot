package com.seckill.seckill_manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("operate_record")
@ApiModel(value = "OperateRecord对象", description = "")
public class OperateRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Integer managerUserId;

    private String managerUserAccount;

    private String operate;

    @ApiModelProperty("0读,1改/登录,2删除")
    private Integer level;

    @ApiModelProperty("如果为增删查改此处为id,分页查询此处为0")
    private Integer operateId;


    @Override
    public String toString() {
        return "OperateRecord{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", managerUserId=" + managerUserId +
                ", managerUserAccount=" + managerUserAccount +
                ", operate=" + operate +
                ", level=" + level +
                ", operateId=" + operateId +
                "}";
    }
}
