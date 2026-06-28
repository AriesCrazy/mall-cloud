package com.czy.mall.controller;

import com.czy.mall.common.result.Result;
import com.czy.mall.entity.Product;
import com.czy.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/product")
//@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/selectById")
    public Result<Product> getById(@RequestParam("id") Long id) {

        // ① 先查 Redis
        String key = "product:" + id;
        Product product = (Product) redisTemplate.opsForValue().get(key);

        if (product != null) {
            return Result.success(product);
        }

        // ② 查 MySQL
        product = productService.getById(id);

        if (product == null) {
            return Result.fail("商品不存在");
        }

        // ③ 放入 Redis
        redisTemplate.opsForValue().set(key, product, Duration.ofMinutes(10));

        // ④ 发送 MQ（记录浏览行为）
        rabbitTemplate.convertAndSend("product.exchange", "product.view", id);

        return Result.success(product);
    }
}
