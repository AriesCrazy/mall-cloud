package com.czy.mall.filter;

import com.czy.mall.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final List<String> WHITELIST = List.of(
            "/user/login",
            "/user/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 白名单
        if (WHITELIST.contains(path)) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null) {
            return unauthorized(exchange);
        }

        // JWT 校验
        if (!JwtUtil.verify(token)) {
            return unauthorized(exchange);
        }

        // Redis 校验
        String key = "login:token:" + token;
        String userJson = stringRedisTemplate.opsForValue().get(key);

        if (userJson == null) {
            return unauthorized(exchange);
        }

        // 可选：传递用户信息给下游服务
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("user", userJson)
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
