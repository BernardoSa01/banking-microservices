package com.example.transaction_service.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class AccountNotFoundExceptionTest {

    @Test
    void shouldCreateException() {
        var ex = new AccountNotFoundException("ACC-1");

        assertNotNull(ex);
        assertTrue(ex.getMessage().contains("ACC-1"));
    }
}
