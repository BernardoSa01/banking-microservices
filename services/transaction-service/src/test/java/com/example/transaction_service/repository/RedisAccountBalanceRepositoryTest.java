package com.example.transaction_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisAccountBalanceRepositoryTest {

    private StringRedisTemplate redis;
    private ValueOperations<String, String> valueOps;
    private RedisAccountBalanceRepository repository;

    @BeforeEach
    void setup() {

        redis = mock(StringRedisTemplate.class);
        valueOps = mock(ValueOperations.class);

        when(redis.opsForValue()).thenReturn(valueOps);

        repository = new RedisAccountBalanceRepository(redis);
    }

    @Test
    void shouldReturnBalance() {

        when(valueOps.get("account:ACC-1:balance")).thenReturn("1000");

        Long balance = repository.getBalance("ACC-1");

        assertEquals(1000L, balance);
    }

    @Test
    void shouldReturnZeroWhenBalanceIsNull() {

        when(valueOps.get(any())).thenReturn(null);

        Long balance = repository.getBalance("ACC-1");

        assertEquals(0L, balance);
    }

    @Test
    void shouldCheckAccountExists() {

        when(redis.hasKey("account:ACC-1:balance")).thenReturn(true);
        when(redis.hasKey("account:ACC-1:limit")).thenReturn(true);

        boolean exists = repository.accountExists("ACC-1");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenDebitExceedsBalance() {

        when(valueOps.decrement(any(), anyLong())).thenReturn(-100L);

        boolean result = repository.debit("ACC-1", 100L);

        assertFalse(result);

        verify(valueOps).increment(any(), anyLong());
    }

    @Test
    void shouldReturnFalseWhenCreditLimitExceeded() {

        when(valueOps.decrement(any(), anyLong())).thenReturn(-50L);

        boolean result = repository.consumeCreditLimit("ACC-1", 100L);

        assertFalse(result);

        verify(valueOps).increment(any(), anyLong());
    }

    @Test
    void shouldReturnFalseWhenAccountDoesNotExist() {

        when(redis.hasKey("account:ACC-1:balance")).thenReturn(true);
        when(redis.hasKey("account:ACC-1:limit")).thenReturn(false);

        boolean result = repository.accountExists("ACC-1");

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenRedisReturnsNullBalance() {

        when(valueOps.decrement(anyString(), anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                repository.debit("ACC-1", 100L)
        );
    }

    @Test
    void shouldThrowExceptionWhenRedisReturnsNullLimit() {

        when(valueOps.decrement(anyString(), anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                repository.consumeCreditLimit("ACC-1", 100L)
        );
    }

    @Test
    void shouldRollbackWhenDebitResultsNegativeBalance() {

        when(valueOps.decrement(anyString(), anyLong())).thenReturn(-50L);

        boolean result = repository.debit("ACC-1", 100L);

        assertFalse(result);

        verify(valueOps).increment(anyString(), anyLong());
    }

    @Test
    void shouldRollbackWhenCreditLimitExceeded() {

        when(valueOps.decrement(anyString(), anyLong())).thenReturn(-10L);

        boolean result = repository.consumeCreditLimit("ACC-1", 100L);

        assertFalse(result);

        verify(valueOps).increment(anyString(), anyLong());
    }
}