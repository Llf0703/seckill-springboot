package com.seckill.seckill_manager.Interceptor;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName LevelCode
 * @description: 权限等级划分
 * @date 2022/3/25 2:28
 */
public interface LevelCode {
    int DENIED = 0;
    int READ = 1;
    int EDIT = 2;
    int OPERATE_READ = 0;
    int OPERATE_EDIT = 1;
    int OPERATE_DELETE = 2;
}
