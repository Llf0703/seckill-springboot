package com.seckill.user_new.entity.RedisService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private String phone;
    private String MD5Password;
    private String fp;
    private String token;
    private String ip;
}
