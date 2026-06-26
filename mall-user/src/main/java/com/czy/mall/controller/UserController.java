package com.czy.mall.controller;

import com.czy.mall.common.result.Result;
import com.czy.mall.dto.LoginDTO;
import com.czy.mall.dto.RegisterDTO;
import com.czy.mall.entity.User;
import com.czy.mall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody User user) {
        return Result.success(userService.save(user));
    }

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.success(userService.register(dto));
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid LoginDTO dto) {

        return Result.success(userService.login(dto));

    }

}
