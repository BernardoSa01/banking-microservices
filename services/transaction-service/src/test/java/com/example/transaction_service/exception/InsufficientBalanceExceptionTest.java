package com.example.transaction_service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientBalanceExceptionTest {

    @Test
    void shouldCreateInsufficientBalanceException() {

        String accountId = "ACC-1";

        InsufficientBalanceException ex = new InsufficientBalanceException(accountId);

        assertNotNull(ex);
        assertTrue(ex.getMessage().contains(accountId));
    }
}
