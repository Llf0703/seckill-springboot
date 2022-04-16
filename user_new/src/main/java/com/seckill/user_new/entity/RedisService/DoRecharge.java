package com.seckill.user_new.entity.RedisService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoRecharge {
    private Integer userId;//用户id
    private String phone;//用户手机号
    private BigDecimal amount;//充值金额
    private Integer rechargeMethod;//充值方式
    private LocalDateTime rechargeTime;//充值时间
    private Long serialNumber;//流水号
}
