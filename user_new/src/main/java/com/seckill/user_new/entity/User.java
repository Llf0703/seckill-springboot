package com.seckill.user_new.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-04-05
 */
@Data
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private String userName;

    private String name;

    private String password;

    private String email;

    private String phone;

    private String idCard;

    @ApiModelProperty("钱包余额")
    private BigDecimal balance;

    private LocalDateTime age;

    @ApiModelProperty("就业状态")
    private Integer employmentStatus;

    @ApiModelProperty("失信状态")
    private Integer creditStatus;

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deletedAt=" + deletedAt +
            ", userName=" + userName +
            ", name=" + name +
            ", password=" + password +
            ", email=" + email +
            ", phone=" + phone +
            ", idCard=" + idCard +
            ", balance=" + balance +
            ", age=" + age +
            ", employmentStatus=" + employmentStatus +
            ", creditStatus=" + creditStatus +
        "}";
    }
}
