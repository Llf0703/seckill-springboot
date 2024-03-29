package com.seckill.user_new.entity.RedisService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCrypto {
    private long timeStamp;
    private String ip;
    private String aesKey;
    private String rsaPubKey;
    private String rsaPrvKey;
    private String captcha;
}
