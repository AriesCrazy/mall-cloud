package com.czy.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    /**
     * BCrypt 加密后的密码
     */
    private String password;

    /**
     * 是否启用
     */
    private Integer status;

    private String nickname;

    private String email;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
