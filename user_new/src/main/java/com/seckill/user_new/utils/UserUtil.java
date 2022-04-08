package com.seckill.user_new.utils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class UserUtil {
    public static String generateUserName() {
        final String S = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            tmp.append(S.charAt(number));
        }
        return tmp.toString();
    }

    public static LocalDateTime getAge(String idCard) throws ParseException {
        String dateString = idCard.substring(6, 14) + " 00:00:00";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return LocalDateTime.parse(dateString,fmt);
    }
}
