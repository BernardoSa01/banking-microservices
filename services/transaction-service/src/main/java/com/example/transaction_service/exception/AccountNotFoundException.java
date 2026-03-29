package com.example.transaction_service.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountId) {

        super("Conta " + accountId + " não encontrada");
    }
}
