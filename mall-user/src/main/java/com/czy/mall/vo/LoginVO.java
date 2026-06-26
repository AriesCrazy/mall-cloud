package com.czy.mall.vo;

import lombok.Data;

@Data
public class LoginVO {

    /**
     * JWT
     */
    private String token;

    /**
     * 过期时间（秒）
     */
    private Long expire;

    /**
     * 当前登录用户
     */
    private UserInfoVO userInfo;
}
