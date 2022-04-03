package com.seckill.seckill_manager;

import com.seckill.seckill_manager.mapper.GoodsMapper;
import com.seckill.seckill_manager.utils.crypto.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;


@SpringBootTest
class SeckillManagerApplicationTests {
    @Resource
    private GoodsMapper goodsMapper;

    @Test
    void contextLoads() throws Exception {
        String enc="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTbTIQFyC6U+YUjLZzXYNO2I880j/meSBvPr85gAWBGKMEObjTfrTGSdBcPFKUSigeQXWcqDeKxmN+NuBxeqs42zUkDtLqKXKFE3XL9zhRXDX6G/KGMibHLBTZZk7mT575LXKhGQ1rI+mElJTzOEST6YUOOPIdW4jhdEjJ037DqQIDAQAB";
        byte[] CIP= RSAUtil.encryptByPublicKey("1742095859".getBytes(StandardCharsets.UTF_8),enc);
        CIP= cn.hutool.core.codec.Base64.decode("aouy19VLS4SyLJ8mTv6rNESPU3cvexCPAKCNYgyZxhRIqr8+LGopWzTrRqjYAAr+IyhMWcRjroMHsXPjCEnO+WaXu6NHV88hy2xafFRPiGUBLPGI3mQCrCjOMdDTOfab6YjI9OTjVfEcRk39N7bN7gQJGefujdHE/f+MO5QQMIg=");
        System.out.println(Base64.encodeBase64String(CIP));
        System.out.println(new String(RSAUtil.decryptByPrivateKey(CIP,"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJNtMhAXILpT5hSMtnNdg07YjzzSP+Z5IG8+vzmABYEYowQ5uNN+tMZJ0Fw8UpRKKB5BdZyoN4rGY3424HF6qzjbNSQO0uopcoUTdcv3OFFcNfob8oYyJscsFNlmTuZPnvktcqEZDWsj6YSUlPM4RJPphQ448h1biOF0SMnTfsOpAgMBAAECgYBM5Syylc2dQjuExmw3ujPOAUic2bE4vj77p0fIbwzv6/DDOHQ95B3YOxHxugj8jb1lIpF814S43odwgqEIr4n0gACycCx56nTMbQ6ruiydTB1jZvZEOcoh9VR3tJJjYogmWkcHOlYxgpSy5jm/v2kYbi4SJoeU/HW3Un1T0JhTgQJBAMwqsPkQURuxZymw0XvSeCJ74kEF7EiqnXrmEn2lRwqf1wQlRt8Ohuw8ramIUCaVbiWF1/e1uYaCR2AskQFs2HECQQC42tDIOeI1MbGk77Z/QnlWSQTSnwzW4wrkuh50xofIx0IToiH+4Yr2y6VmmYeZDQZNRLdACDtkAo1JHuiD+Pq5AkEAuyN6eaDqJ08Rt1YafbuqYDlbZqxuKyEwHeFbG43uMx/VrzgX4QboxwrLR3TpuqQHhR9MmQIaHt+1WsMe2saREQJATixjLLml2v8H9AtXrxuqen3opzWR1XIwkifPZ2zp0VPKssjW/ZXeEZ5vdSMqP+RNLPIJWO/iRdn6a9jBVcY+KQJAS6DPPP3fg3jy2CCs8kCQzNRWfNDUsgCqDqBgVJ0vYieng1+IMhDmGjIP8dHXSPINwXS2r717VHTxyVcuuwBabw==")));
    }

}
