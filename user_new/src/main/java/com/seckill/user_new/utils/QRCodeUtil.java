package com.seckill.user_new.utils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

public class QRCodeUtil {
    public static String generatorQR(String url) {
        QrConfig config = new QrConfig();
        //config.setErrorCorrection(ErrorCorrectionLevel.H);
        return QrCodeUtil.generateAsBase64(url, config, "png");
    }
}
