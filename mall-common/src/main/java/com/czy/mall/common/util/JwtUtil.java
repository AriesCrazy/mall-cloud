package com.czy.mall.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "iloveu";

    /**
     * 生成Token
     */
    public static String createToken(Long userId, String username) {

        Map<String, Object> payload = new HashMap<>();

        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("exp",
                DateUtil.date().offsetNew(
                        cn.hutool.core.date.DateField.SECOND,
                        7200
                ));

        return JWTUtil.createToken(payload, SECRET.getBytes());
    }

    /**
     * 校验Token
     */
    public static boolean verify(String token) {
        return JWTUtil.verify(token, SECRET.getBytes());
    }

    /**
     * 解析JWT
     */
    public static JWT parse(String token) {
        return JWTUtil.parseToken(token);
    }
}
