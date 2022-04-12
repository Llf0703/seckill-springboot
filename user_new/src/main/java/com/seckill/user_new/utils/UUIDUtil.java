package com.seckill.user_new.utils;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.IdcardUtil;
import org.yaml.snakeyaml.tokens.Token;

public class UUIDUtil {
    public static String getUUID(){
        return IdUtil.simpleUUID();
    }
}
