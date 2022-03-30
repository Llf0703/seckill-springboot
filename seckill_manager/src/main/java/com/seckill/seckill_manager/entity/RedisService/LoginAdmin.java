package com.seckill.seckill_manager.entity.RedisService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAdmin {
    private String account;
    private String MD5Password;
    private String fp;
}
