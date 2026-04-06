package com.example.transaction_service.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisAccountBalanceRepository implements AccountBalanceRepository {

    private final StringRedisTemplate redis;

    public RedisAccountBalanceRepository(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String balanceKey(String accountId) {
        return "account:" + accountId + ":balance";
    }

    private String limitKey(String accountId) {
        return "account:" + accountId + ":limit";
    }

    @Override
    public Long getBalance(String accountId) {
        String value = redis.opsForValue().get(balanceKey(accountId));
        return value != null ? Long.parseLong(value) : 0L;
    }

    @Override
    public Long getCreditLimit(String accountId) {
        String value = redis.opsForValue().get(limitKey(accountId));
        return value != null ? Long.parseLong(value) : 0L;
    }

    @Override
    public boolean debit(String accountId, Long amount) {
        Long newBalance = redis.opsForValue().decrement(balanceKey(accountId), amount);

        if (newBalance < 0) {
            redis.opsForValue().increment(balanceKey(accountId), amount);
            return false;
        }

        return true;
    }

    @Override
    public boolean consumeCreditLimit(String accountId, Long amount) {
        Long newLimit = redis.opsForValue().decrement(limitKey(accountId), amount);

        if (newLimit < 0) {
            redis.opsForValue().increment(limitKey(accountId), amount);
            return false;
        }

        return true;
    }

    @Override
    public boolean accountExists(String accountId) {

        Boolean balanceExists = redis.hasKey(balanceKey(accountId));
        Boolean limitExists = redis.hasKey(limitKey(accountId));

        return Boolean.TRUE.equals(balanceExists) && Boolean.TRUE.equals(limitExists);
    }
}
