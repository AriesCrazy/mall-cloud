package com.czy.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.mall.common.result.Result;
import com.czy.mall.dto.LoginDTO;
import com.czy.mall.dto.RegisterDTO;
import com.czy.mall.dto.UserDTO;
import com.czy.mall.entity.User;

public interface UserService extends IService<User> {
    boolean register(RegisterDTO dto);

    String login(LoginDTO dto);

}
