package com.example.transaction_service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void shouldCreateAccountNotFoundException() {
        var ex = new AccountNotFoundException("ACC-1");
        assertNotNull(ex);
        assertTrue(ex.getMessage().contains("ACC-1"));
    }

    @Test
    void shouldCreateInsufficientBalanceException() {
        var ex = new InsufficientBalanceException("ACC-1");
        assertNotNull(ex);
    }

    @Test
    void shouldCreateCreditLimitExceededException() {
        var ex = new CreditLimitExceededException("ACC-1");
        assertNotNull(ex);
    }
}