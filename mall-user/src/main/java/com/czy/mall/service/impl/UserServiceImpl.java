package com.czy.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.mall.common.util.JwtUtil;
import com.czy.mall.dto.LoginDTO;
import com.czy.mall.dto.RegisterDTO;
import com.czy.mall.entity.User;
import com.czy.mall.mapper.UserMapper;
import com.czy.mall.service.UserService;
import com.czy.mall.vo.LoginVO;
import com.czy.mall.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
            throw new RuntimeException("用户名不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = JwtUtil.createToken(user.getId(), user.getUsername());

        stringRedisTemplate.opsForValue().set(
                "login:" + token,
                JSONUtil.toJsonStr(user),
                Duration.ofHours(2)
        );

        return token;
    }
}
