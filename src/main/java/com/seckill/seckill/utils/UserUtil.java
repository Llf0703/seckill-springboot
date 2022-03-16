package com.seckill.seckill.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.seckill.seckill.entity.Manager;
import com.seckill.seckill.entity.User;

import org.springframework.util.DigestUtils;

public class UserUtil {

    private static boolean is_valid_phone(String phone) {
        if (phone == null) return false;
        return Pattern.matches("^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", phone);
    }

    private static boolean is_valid_id_card(String id_card) {
        if (id_card == null) return false;
        return IdCardUtil.idCardValidate(id_card);
    }

    private static boolean is_valid_password(String password) {
        if (password == null) return false;
        return Pattern.matches("^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])).{6,20}$", password);
    }

    private static boolean is_valid_account(String account) {
        if (account == null) return false;
        return Pattern.matches("^[A-Za-z0-9]{1,10}$", account);
    }

    private static boolean is_valid_name(String name) {
        if (name == null) return false;
        return Pattern.matches("^[\u9FA6-\u9FCB\u3400-\u4DB5\u4E00-\u9FA5]{2,5}([\u25CF\u00B7][\u9FA6-\u9FCB\u3400-\u4DB5\u4E00-\u9FA5]{2,5})*$", name);
    }
    
    public static MessageUitl register_check(User user) {
        MessageUitl result = new MessageUitl();
        if (!is_valid_phone(user.getPhone())) result.auth_error("invalid phone");
        else if (!is_valid_password(user.getPassword())) result.auth_error("invalid password");
        else if (!is_valid_id_card(user.getId_card())) result.auth_error("invalid id card");
        else if (!is_valid_name(user.getName())) result.auth_error("invalid name");
        else result.success("ok");
        return result;
    }

    public static MessageUitl login_check(User user) {
        MessageUitl result = new MessageUitl();
        if (!is_valid_phone(user.getPhone())) result.auth_error("invalid phone");
        else if (!is_valid_password(user.getPassword())) result.auth_error("invalid password");
        else result.success("ok");
        return result;
    }

    public static MessageUitl login_check(Manager user) {
        MessageUitl result = new MessageUitl();
        if (!is_valid_account(user.getAccount())) result.auth_error("invalid account");
        else if (!is_valid_password(user.getPassword())) result.auth_error("invalid password");
        else result.success("ok");
        return result;
    }

    public static String password_to_md5(String phone, String password) {
        final String salt = "23fdt34w=-*.de";
        String md5 = DigestUtils.md5DigestAsHex(phone.concat(password).concat(salt).getBytes());
        return md5;
    }

    public static Date get_age(String id_card) throws ParseException {
        String date_string = id_card.substring(6, 14);
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        fmt.setLenient(false); 
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return fmt.parse(date_string);
    }

    public static String generate_user_name() {
        final String S = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            tmp.append(S.charAt(number));
        }
        return tmp.toString();
    }
}
