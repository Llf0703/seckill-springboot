package com.seckill.seckill_manager.controller.vo;

import lombok.Data;

/*
 * @MethodName 前端传回用户数据
 * @author Wky1742095859
 * @Description
 * @Date 2022/3/19 1:29
 * @Param
 * @Return
 **/
@Data
public class ManagerUsersVO {
    private Integer id;

    private String account;

    private String password;

    private Integer seckillItemsPermissions;

    private Integer seckillRecordPermissions;

    private Integer rechargeRecordPermissions;

    private Integer adminInfoPermissions;

    private Integer financialItemsPermissions;

    private Integer riskControlPermissions;

    private Integer guestInfoPermissions;

    private String token;//非数据库字段,用于添加额外的安全校验信息

    private String uid;//登录环节需要
}
