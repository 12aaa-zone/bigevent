package com.as.oblog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

/**
 * @author 12aaa-zone
 */


public class JwtUtil {

    //TODO  这里后期会用springsecurity重构
    private static String KEY_PATH = "src/main/resources/public-key.pem";
    private static String KEY= "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2h2K0wsC69wQFYHmF02P\n" +
            "odd50T/afE7r/2n3FPUZOIemBv6S6stzAQeMjdCtIfK/WAiNSh6dsX3ixN3J8xeF\n" +
            "yi7suYV/dRE+CW5r9qs7zg31hpSIt1KhO6H2lv/q6DGp8F5T//59pvaikgDpIaSK\n" +
            "upnrNCmU5t9EhAaYOkctc44y2+iVsJX5UmujHkDDH7AgONFQuKb/WV7lhIerlCPb\n" +
            "zplV0XIabEMDEFdXhJysBrpynA8tTvxGyt+gNEV2+ycPzbiyGpvCgBimzko1m8o9\n" +
            "a23RVlsN5XrmT/JfTw94M/pakbaNqRXrALnszjgLlfbb3lsl5qKwVf3p5jWMD4ni\n" +
            "xQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

   /*
    static {
        try {
            KEY = new String(Files.readAllBytes(Paths.get(KEY_PATH)));
        } catch (IOException e) {
            KEY = "NO_PEM_FILES";
            throw new RuntimeException("读取密钥文件失败: " + e.getMessage(), e);
        }
    }*/


    // 默认一个小时的token
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(KEY));
    }

    //接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
