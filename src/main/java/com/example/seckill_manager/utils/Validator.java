package com.example.seckill_manager.utils;

import java.util.regex.Pattern;


interface RegexStr {
    String REGEX_ACCOUNT = "^[A-Za-z0-9]{1,10}$";
    String REGEX_PASSWORD = "^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])).{6,20}$";
}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName Validator
 * @description: 校验参数
 * @date 2022/3/19 1:41
 */
public class Validator {
    /*
     * @MethodName isValidAccount
     * @author Wky1742095859
     * @Description 校验账号是否合法
     * @Date 2022/3/19 1:47
     * @Param [account]
     * @Return boolean
     **/
    public static boolean isValidAccount(String account) {
        return Pattern.matches(RegexStr.REGEX_ACCOUNT, account);
    }
    /*
     * @MethodName isValidPassword
     * @author Wky1742095859
     * @Description 校验密码是否合法
     * @Date 2022/3/19 19:14
     * @Param [password]
     * @Return boolean
    **/
    public static boolean isValidPassword(String password) {
        return Pattern.matches(RegexStr.REGEX_PASSWORD, password);
    }
}
