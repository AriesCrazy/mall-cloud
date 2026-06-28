package com.czy.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.mall.common.result.Result;
import com.czy.mall.common.util.JwtUtil;
import com.czy.mall.dto.LoginDTO;
import com.czy.mall.dto.RegisterDTO;
import com.czy.mall.dto.UserDTO;
import com.czy.mall.entity.User;
import com.czy.mall.mapper.UserMapper;
import com.czy.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(RegisterDTO dto) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();

        wrapper.eq(User::getUsername, dto.getUsername());

        if (this.count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        // BCrypt 加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return this.save(user);
    }

    @Override
    public String login(LoginDTO dto) {

        User user = lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .one();

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = JwtUtil.createToken(user.getId(), user.getUsername());

        stringRedisTemplate.opsForValue().set(
                "login:token:" + token,
                JSONUtil.toJsonStr(user),
                Duration.ofHours(2)
        );

        return token;
    }

    @Transactional
    @Override
    public Result<String> findUsersByCondition(UserDTO userDTO) {
        if (isAllEmpty(userDTO)) {
            throw new RuntimeException("参数不能为空");
        }
        List<User> users = userMapper.selectByUser(userDTO);
        if (users.size()==0) {
            throw new RuntimeException("用户不存在");
        }
        return Result.success(users.toString());
    }

    private boolean isAllEmpty(UserDTO dto) {
        return dto.getId() == null
                && (dto.getUsername() == null || dto.getUsername().isEmpty())
                && dto.getStatus() == null
                && (dto.getNickname() == null || dto.getNickname().isEmpty())
                && (dto.getEmail() == null || dto.getEmail().isEmpty())
                && dto.getCreateTime() == null
                && dto.getUpdateTime() == null;
    }
}
