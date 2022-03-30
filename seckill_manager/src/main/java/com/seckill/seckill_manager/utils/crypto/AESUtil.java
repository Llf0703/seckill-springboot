package com.seckill.seckill_manager.utils.crypto;

import com.baomidou.mybatisplus.core.toolkit.AES;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName AES工具类
 * @description: AES加密
 * @date 2022/3/29 23:25
 */
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

    /*
     * @MethodName encrypt
     * @author Wky1742095859
     * @Description AES加密
     * @Date 2022/3/30 0:07
     * @Param [content, password]
     * @Return java.lang.String
    **/
    public static String encrypt(String content, String password) {
        try {
            return AES.encrypt(content, password);
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * @MethodName decrypt
     * @author Wky1742095859
     * @Description AES解密
     * @Date 2022/3/30 0:06
     * @Param [content, password]
     * @Return java.lang.String
    **/
    public static String decrypt(String content, String password) {
        try {
            return AES.decrypt(content, password);
        } catch (Exception e) {
            return null;
        }

    }

    /*
     * @MethodName getSecretKey
     * @author Wky1742095859
     * @Description 生成随机密钥
     * @Date 2022/3/30 0:06
     * @Param []
     * @Return java.lang.String
    **/
    public static String getSecretKey() {
        try {
            return AES.generateRandomKey();
        } catch (Exception e) {
            return null;
        }
    }
}


class AESUtilBackUp{
    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param content 加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);

    }

    /**
     * 解密
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }
}