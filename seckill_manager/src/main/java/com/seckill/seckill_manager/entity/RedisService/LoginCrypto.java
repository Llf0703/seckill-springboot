package com.seckill.seckill_manager.entity.RedisService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCrypto {
    private long timeStamp;
    private String ip;
    private String fp;
    private String aesKey;
    private String rsaPubKey;
    private String rsaPrvKey;
}
