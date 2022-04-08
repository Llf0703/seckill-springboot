package com.seckill.user_new.controller.vo;

import lombok.Data;
/**
 * @ClassName RegisterVO
 * @description:  注册接口json
 * @author Wky1742095859
 * @date 2022/4/7 20:27
 * @version 1.0
 */
@Data
public class RegisterVO {
    private Integer id;

    private String name;

    private String password;

    private String phone;

    private String id_card;

    private String uid;

    private String token;

    private String captcha;
}