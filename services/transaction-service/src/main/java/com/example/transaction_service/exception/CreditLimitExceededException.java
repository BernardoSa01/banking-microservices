package com.example.transaction_service.exception;

public class CreditLimitExceededException extends RuntimeException {
    public CreditLimitExceededException(String accountId) {

        super("Limite insuficiente para a conta: " + accountId);
    }
}
