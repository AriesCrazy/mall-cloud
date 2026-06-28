package com.czy.mall.controller;

import com.czy.mall.common.result.Result;
import com.czy.mall.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello Mall User");
    }

    @GetMapping("/config")
    public String config() {
        return secret;
    }
}
