package com.example.transaction_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleAccountNotFoundException() {

        AccountNotFoundException ex = new AccountNotFoundException("ACC-1");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/transactions");

        ResponseEntity<Map<String, Object>> response =
                handler.handleAccountNotFound(ex, request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("/transactions", response.getBody().get("path"));
    }

    @Test
    void shouldHandleInsufficientBalanceException() {
        InsufficientBalanceException ex = new InsufficientBalanceException("ACC-1");
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/transactions");

        ResponseEntity<Map<String, Object>> resp = handler.handleInsufficientBalance(ex, req);

        assertEquals(400, resp.getStatusCode().value());
        assertEquals("Bad Request", resp.getBody().get("error"));
    }

    @Test
    void shouldHandleCreditLimitExceededException() {
        CreditLimitExceededException ex = new CreditLimitExceededException("ACC-1");
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/transactions");

        ResponseEntity<Map<String, Object>> resp = handler.handleCreditLimitExceeded(ex, req);

        assertEquals(400, resp.getStatusCode().value());
        assertEquals("Bad Request", resp.getBody().get("error"));
    }
}