package com.czy.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.mall.dto.LoginDTO;
import com.czy.mall.dto.RegisterDTO;
import com.czy.mall.entity.User;
import com.czy.mall.vo.LoginVO;

public interface UserService extends IService<User> {
    boolean register(RegisterDTO dto);

    String login(LoginDTO dto);
}
