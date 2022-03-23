package com.seckill.seckill_manager.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;


interface RegexStr {
    String REGEX_ACCOUNT = "^[A-Za-z0-9]{1,10}$";
    String REGEX_PASSWORD = "^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])).{6,20}$";
    String REGEX_PRODUCT_NAME = "^[\u4E00-\u9FA5A-Za-z0-9_]{1,20}$";
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
        if (account == null) return false;
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
        if (password == null) return false;
        return Pattern.matches(RegexStr.REGEX_PASSWORD, password);
    }

    public static boolean isValidAmountCanBeZERO(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(BigDecimal.ZERO) >= 0 && amount.compareTo(BigDecimal.valueOf(99999999999.9999)) <= 0;
    }

    public static boolean isValidAmountCanNotBeZERO(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(BigDecimal.valueOf(99999999999.9999)) <= 0;
    }

    public static boolean isValidProductName(String str) {
        if (str == null) return false;
        return Pattern.matches(RegexStr.REGEX_PRODUCT_NAME, str);
    }

    public static boolean isValidZeroOrOne(int n) {
        return n == 1 || n == 0;
    }

    public static boolean isValidRate(BigDecimal rate) {
        BigDecimal temp=rate.divide(new BigDecimal("100"),8, RoundingMode.HALF_UP);
        return temp.compareTo(BigDecimal.ZERO) >= 0 && temp.compareTo(BigDecimal.ONE) <= 0;

    }

    public static boolean isValidShelfLife(int n) {
        return n >= 1 && n <= 36000;
    }
}
