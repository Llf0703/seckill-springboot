package com.seckill.seckill_manager.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.ShearCaptcha;

import java.util.HashMap;

public class Captcha {
    public static HashMap<String,Object> getCircleCaptcha(){
        CircleCaptcha captcha= CaptchaUtil.createCircleCaptcha(250,80,5,20);
        HashMap<String,Object> cap=new HashMap<>();
        cap.put("code",captcha.getCode());
        cap.put("img",captcha.getImageBase64Data());
        //System.out.println(captcha.getImageBase64());
        //System.out.println(captcha.getImageBase64Data());
        return cap;
    }
    public static String getGifCaptcha(){
        GifCaptcha gifCaptcha=CaptchaUtil.createGifCaptcha(250,80,5);
        //gifCaptcha.createCode();
        gifCaptcha.setQuality(1);
        gifCaptcha.write("e:/test.gif");
        return gifCaptcha.getCode();
    }
    public  static String getShearCaptcha(){
        ShearCaptcha shearCaptcha=CaptchaUtil.createShearCaptcha(250,80,5,4);
        shearCaptcha.write("e:/test.jpg");
        return "ok";

    }
}
