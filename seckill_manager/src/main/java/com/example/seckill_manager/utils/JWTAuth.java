package com.example.seckill_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

interface JWT_PARAMS {
    long EXPIRE_TIME = 60 * 60 * 1000; //ms
    String SECRET_KEY = "34fcjhs-*2d=.df3";
}


public class JWTAuth {
    public static String releaseToken(String account) {
        Date expire_at = new Date(System.currentTimeMillis() + JWT_PARAMS.EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(JWT_PARAMS.SECRET_KEY);
        return JWT.create().withClaim("user", account)
                .withExpiresAt(expire_at)
                .sign(algorithm);
    }

    public static String releaseToken(String account, long expire_time_second) {
        Date expire_at = new Date(System.currentTimeMillis() + expire_time_second * 1000);
        Algorithm algorithm = Algorithm.HMAC256(JWT_PARAMS.SECRET_KEY);
        return JWT.create().withClaim("user", account)
                .withExpiresAt(expire_at)
                .sign(algorithm);
    }

    public static HashMap<String,Object> parseToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_PARAMS.SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            String account = jwt.getClaim("user").asString();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("status",true);
            hashMap.put("account",account);
            return hashMap;
        } catch (Exception e) {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("status",false);
            return hashMap;
        }
    }
}
