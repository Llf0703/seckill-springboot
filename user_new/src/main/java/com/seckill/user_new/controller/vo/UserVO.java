package com.seckill.user_new.controller.vo;

import lombok.Data;

@Data
public class UserVO {
    private Integer id;

    private String name, password, phone, id_card, uid, token, captcha;
}
