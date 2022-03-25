package com.seckill.seckill_manager.utils;
import org.springframework.util.DigestUtils;
interface Salt{
    String PASSWORD_SALT="23fdt34w=-*.de";
}

/**
 * @ClassName MD5
 * @description: MD5处理类
 * @author Wky1742095859
 * @date 2022/3/25 2:35
 * @version 1.0
 */
public class MD5 {
    /*
     * @MethodName MD5Password
     * @author Wky1742095859
     * @Description 传入字符串+盐转MD5
     * @Date 2022/3/25 2:35
     * @Param [str]
     * @Return java.lang.String
    **/
    public static String MD5Password(String str){
        return DigestUtils.md5DigestAsHex(str.concat(Salt.PASSWORD_SALT).getBytes());
    }
}
