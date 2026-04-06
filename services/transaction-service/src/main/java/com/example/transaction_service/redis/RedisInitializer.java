package com.example.transaction_service.redis;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer {
    private final StringRedisTemplate redis;

    public RedisInitializer(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @PostConstruct
    public void init() {
        redis.opsForValue().set("account:ACC-1:balance", "10000");
        redis.opsForValue().set("account:ACC-1:limit", "5000");

        redis.opsForValue().set("account:ACC-2:balance", "10000");
        redis.opsForValue().set("account:ACC-2:limit", "5000" );

        System.out.println("Redis accounts initialized");
    }
}
