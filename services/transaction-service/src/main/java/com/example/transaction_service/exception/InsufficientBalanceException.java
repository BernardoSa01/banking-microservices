package com.example.transaction_service.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String accountId) {
        super("Saldo insuficiente para a conta: " + accountId);
    }
}
