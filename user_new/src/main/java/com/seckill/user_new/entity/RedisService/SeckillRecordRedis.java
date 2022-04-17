package com.seckill.user_new.entity.RedisService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillRecordRedis {
    private Integer userId;//用户id
    private String phone;//用户手机号
    private LocalDateTime seckillTime;//秒杀时间
    private BigDecimal amount;
    private Integer status;
    private Integer seckillItemsId;
    private Long serialNumber;//流水号
}
