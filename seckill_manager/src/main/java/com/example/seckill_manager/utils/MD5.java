package com.example.seckill_manager.utils;
import org.springframework.util.DigestUtils;
interface Salt{
    String PASSWORD_SALT="23fdt34w=-*.de";
}
public class MD5 {
    public static String MD5Password(String str){
        return DigestUtils.md5DigestAsHex(str.concat(Salt.PASSWORD_SALT).getBytes());
    }
}
