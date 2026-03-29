package com.example.transaction_service.repository;

public interface AccountBalanceRepository {

    Long getBalance(String accountId);

    Long getCreditLimit(String accountId);

    boolean debit(String accountId, Long amount);

    boolean consumeCreditLimit(String accountId, Long amount);

    boolean accountExists(String accountId);
}
