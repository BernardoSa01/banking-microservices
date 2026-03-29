package com.example.transaction_service.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountBalanceRepository implements AccountBalanceRepository {

    private final Map<String, Long> balances = new ConcurrentHashMap<>();
    private final Map<String, Long> creditLimits = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        balances.put("ACC-1", 5000L);
        balances.put("ACC-2", 2000L);

        creditLimits.put("ACC-1", 3000L);
        creditLimits.put("ACC-2", 1000L);
    }

    @Override
    public Long getBalance(String accountId) {
        return balances.getOrDefault(accountId, 0L);
    }

    @Override
    public Long getCreditLimit(String accountId) {
        return creditLimits.getOrDefault(accountId, 0L);
    }


    // synchronized: garante que duas requisições simultâneas não alterem o saldo ao mesmo tempo
    public synchronized boolean debit(String accountId, Long amount) {
        Long currentBalance = balances.getOrDefault(accountId, 0L);

        if (currentBalance < amount) {
            return false;
        }

        balances.put(accountId, currentBalance - amount);
        return true;
    }

    public synchronized boolean consumeCreditLimit(String accountId, Long amount) {
        Long currentLimit = creditLimits.getOrDefault(accountId, 0L);

        if (currentLimit < amount) {
            return false;
        }

        creditLimits.put(accountId, currentLimit - amount);
        return true;
    }

    @Override
    public boolean accountExists(String accountId) {
        return balances.containsKey(accountId) || creditLimits.containsKey(accountId);
    }

}
