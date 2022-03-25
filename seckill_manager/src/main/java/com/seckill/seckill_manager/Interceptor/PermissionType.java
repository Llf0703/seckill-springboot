package com.seckill.seckill_manager.Interceptor;
/**
 * @ClassName PermissionType
 * @description:  权限范围划分
 * @author Wky1742095859
 * @date 2022/3/25 2:28
 * @version 1.0
 */
public interface PermissionType {
    int FinancialItemPermission = 1;
    int SeckillItemPermission = 2;
    int SeckillRecordPermission = 3;
    int RechargeRecordPermission=4;
    int AdminInfoPermission=5;
    int RiskControlPermission=6;
    int GuestInfoPermission=7;
}
