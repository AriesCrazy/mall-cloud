package com.czy.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.mall.entity.User;
import com.czy.mall.mapper.UserMapper;
import com.czy.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }
}
