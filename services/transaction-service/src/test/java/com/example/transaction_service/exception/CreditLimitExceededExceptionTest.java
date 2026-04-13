package com.example.transaction_service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditLimitExceededExceptionTest {

    @Test
    void shouldCreateCreditLimitExceededException() {

        String accountId = "ACC-1";

        CreditLimitExceededException ex = new CreditLimitExceededException(accountId);

        assertNotNull(ex);
        assertTrue(ex.getMessage().contains(accountId));
    }
}
