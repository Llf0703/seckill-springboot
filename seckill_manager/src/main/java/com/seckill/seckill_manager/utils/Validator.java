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

    /*
     * @MethodName isValidAmountCanBeZERO
     * @author Wky1742095859
     * @Description 校验金额是否合法(可为0)
     * @Date 2022/3/25 2:36
     * @Param [amount]
     * @Return boolean
     **/
    public static boolean isValidAmountCanBeZERO(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(BigDecimal.ZERO) >= 0 && amount.compareTo(new BigDecimal("99999999999.9999")) <= 0;
    }

    /*
     * @MethodName isValidAmountCanNotBeZERO
     * @author Wky1742095859
     * @Description 校验金额是否合法(不可为0)
     * @Date 2022/3/25 2:36
     * @Param [amount]
     * @Return boolean
     **/
    public static boolean isValidAmountCanNotBeZERO(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(new BigDecimal("99999999999.9999")) <= 0;
    }

    /*
     * @MethodName isValidProductName
     * @author Wky1742095859
     * @Description 校验名称是否合法
     * @Date 2022/3/25 2:36
     * @Param [str]
     * @Return boolean
     **/
    public static boolean isValidProductName(String str) {
        if (str == null) return false;
        return Pattern.matches(RegexStr.REGEX_PRODUCT_NAME, str);
    }

    /*
     * @MethodName isValidZeroOrOne
     * @author Wky1742095859
     * @Description 校验是否为0或1
     * @Date 2022/3/25 2:36
     * @Param [n]
     * @Return boolean
     **/
    public static boolean isValidZeroOrOne(int n) {
        return n == 1 || n == 0;
    }

    /*
     * @MethodName isValidRate
     * @author Wky1742095859
     * @Description 校验百分数是否合法[0,1]
     * @Date 2022/3/25 2:37
     * @Param [rate]
     * @Return boolean
     **/
    public static boolean isValidRate(BigDecimal rate) {
        if (rate == null) return false;
        BigDecimal temp = rate.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);//百分数化小数
        return temp.compareTo(BigDecimal.ZERO) >= 0 && temp.compareTo(BigDecimal.ONE) <= 0;
    }

    /*
     * @MethodName isValidAge
     * @author Wky1742095859
     * @Description 校验年龄是否合法[0,100]
     * @Date 2022/3/25 2:37
     * @Param [age]
     * @Return boolean
     **/
    public static boolean isValidAge(Integer age) {
        if (age == null) return false;
        return age >= 0 && age <= 100;
    }

    /*
     * @MethodName isValidTimes
     * @author Wky1742095859
     * @Description 校验次数是否合法[0,65535]
     * @Date 2022/3/25 2:37
     * @Param [times]
     * @Return boolean
     **/
    public static boolean isValidTimes(Integer times) {
        if (times == null) return false;
        return times >= 0 && times <= 65535;
    }

    /*
     * @MethodName isValidDays
     * @author Wky1742095859
     * @Description 校验天数是否合法[1,36000]
     * @Date 2022/3/25 2:37
     * @Param [n]
     * @Return boolean
     **/
    public static boolean isValidDays(Integer n) {
        if (n == null) return false;
        return n >= 1 && n <= 36000;
    }

    /*
     * @MethodName isValidPageCurrent
     * @author Wky1742095859
     * @Description 校验页数是否合法
     * @Date 2022/3/25 2:38
     * @Param [current]
     * @Return boolean
     **/
    public static boolean isValidPageCurrent(Integer current) {
        return current != null;
    }

    /*
     * @MethodName isValidPageSize
     * @author Wky1742095859
     * @Description 校验页大小是否合法(0,50]
     * @Date 2022/3/25 2:38
     * @Param [size]
     * @Return boolean
     **/
    public static boolean isValidPageSize(Integer size) {
        if (size == null) return false;
        return size > 0 && size <= 50;
    }
}
