package com.example.transaction_service.service;

import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.exception.AccountNotFoundException;
import com.example.transaction_service.exception.CreditLimitExceededException;
import com.example.transaction_service.exception.InsufficientBalanceException;
import com.example.transaction_service.kafka.TransactionProducer;
import com.example.transaction_service.mapper.TransactionMapper;
import com.example.transaction_service.model.Transaction;
import com.example.transaction_service.model.TransactionStatus;
import com.example.transaction_service.model.TransactionType;
import com.example.transaction_service.repository.AccountBalanceRepository;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionMapper mapper;
    private AccountBalanceRepository repository;
    private TransactionProducer producer;
    private MeterRegistry meterRegistry;

    private TransactionService service;

    @BeforeEach
    void setup() {

        mapper = Mockito.mock(TransactionMapper.class);
        repository = Mockito.mock(AccountBalanceRepository.class);
        producer = Mockito.mock(TransactionProducer.class);

        meterRegistry = new SimpleMeterRegistry();

        service = new TransactionService(mapper, repository, producer, meterRegistry);
    }

    private Transaction buildTransaction(TransactionType type, Long amount) {
        Transaction tx = new Transaction();
        tx.setAccountId("ACC-1");
        tx.setType(type);
        tx.setAmount(amount);
        return tx;
    }

    @Test
    void shouldApproveDebitTransaction() {

        TransactionRequestDTO dto = new TransactionRequestDTO("ACC-1", TransactionType.DEBIT, 100L);
        Transaction tx = buildTransaction(TransactionType.DEBIT, 100L);

        when(repository.accountExists("ACC-1")).thenReturn(true);
        when(repository.debit("ACC-1", 100L)).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(tx);

        TransactionResponseDTO responseDto = new TransactionResponseDTO();
        responseDto.setStatus(TransactionStatus.APPROVED);
        responseDto.setAmount(100L);
        responseDto.setType(TransactionType.DEBIT);

        when(mapper.toDto(any())).thenReturn(responseDto);

        TransactionResponseDTO response = service.processTransaction(dto);

        assertEquals(TransactionStatus.APPROVED, response.getStatus());

        verify(producer, times(1)).sendTransactionEvent(any());
    }

    @Test
    void shouldRejectDebitTransactionWhenBalanceInsufficient() {

        TransactionRequestDTO dto = new TransactionRequestDTO("ACC-1", TransactionType.DEBIT, 100L);
        Transaction tx = buildTransaction(TransactionType.DEBIT, 100L);

        when(repository.accountExists("ACC-1")).thenReturn(true);
        when(repository.debit("ACC-1", 100L)).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(tx);

        assertThrows(InsufficientBalanceException.class, () -> service.processTransaction(dto));
    }

    @Test
    void shouldApproveCreditTransaction() {

        TransactionRequestDTO dto = new TransactionRequestDTO(
                "ACC-1",
                TransactionType.CREDIT,
                200L
        );

        Transaction tx = buildTransaction(TransactionType.CREDIT, 200L);

        when(repository.accountExists("ACC-1")).thenReturn(true);
        when(repository.consumeCreditLimit("ACC-1", 200L)).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(tx);

        TransactionResponseDTO responseDto = new TransactionResponseDTO();
        responseDto.setStatus(TransactionStatus.APPROVED);
        responseDto.setAmount(200L);
        responseDto.setType(TransactionType.CREDIT);

        when(mapper.toDto(any())).thenReturn(responseDto);

        TransactionResponseDTO response = service.processTransaction(dto);

        assertNotNull(response);
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
        assertEquals(200L, response.getAmount());
        assertEquals(TransactionType.CREDIT, response.getType());

        verify(producer, times(1)).sendTransactionEvent(any());
    }


    @Test
    void shouldRejectCreditTransactionWhenLimitExceeded() {

        TransactionRequestDTO dto = new TransactionRequestDTO("ACC-1", TransactionType.CREDIT, 200L);
        Transaction tx = buildTransaction(TransactionType.CREDIT, 200L);

        when(repository.accountExists("ACC-1")).thenReturn(true);
        when(repository.consumeCreditLimit("ACC-1", 200L)).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(tx);

        assertThrows(CreditLimitExceededException.class, () -> service.processTransaction(dto));
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        TransactionRequestDTO dto = new TransactionRequestDTO("ACC-1", TransactionType.DEBIT, 100L);

        when(repository.accountExists("ACC-1")).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> service.processTransaction(dto));
    }
}
