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

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        // 放行登录接口
        if (path.contains("/login") || path.contains("/register")) {
            return chain.filter(exchange);
        }

        // 获取 token
        String token = request.getHeaders().getFirst("Authorization");

        if (token == null || token.isEmpty()) {
            return unauthorized(exchange);
        }

        // 校验 JWT
        if (!JwtUtil.verify(token)) {
            return unauthorized(exchange);
        }

        // 校验 Redis
        String userJson = stringRedisTemplate.opsForValue()
                .get("login:" + token);

        if (userJson == null) {
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
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
