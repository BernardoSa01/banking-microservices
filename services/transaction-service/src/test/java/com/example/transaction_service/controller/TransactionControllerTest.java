package com.example.transaction_service.controller;

import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.model.TransactionStatus;
import com.example.transaction_service.model.TransactionType;
import com.example.transaction_service.repository.AccountBalanceRepository;
import com.example.transaction_service.service.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    private TransactionService transactionService;
    private AccountBalanceRepository repository;

    private TransactionController controller;

    @BeforeEach
    void setup() {

        transactionService = Mockito.mock(TransactionService.class);
        repository = Mockito.mock(AccountBalanceRepository.class);

        controller = new TransactionController(transactionService, repository);
    }

    @Test
    void shouldExecuteTransactionSuccessfully() {

        TransactionRequestDTO request = new TransactionRequestDTO(
                "ACC-1",
                TransactionType.DEBIT,
                100L
        );

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setTransactionId("tx-1");
        response.setStatus(TransactionStatus.APPROVED);
        response.setAmount(100L);
        response.setType(TransactionType.DEBIT);

        when(transactionService.processTransaction(request)).thenReturn(response);

        ResponseEntity<TransactionResponseDTO> result =
                controller.executeTransaction(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("tx-1", result.getBody().getTransactionId());

        verify(transactionService, times(1)).processTransaction(request);
    }

    @Test
    void shouldReturnAccountBalance() {

        when(repository.getBalance("ACC-1")).thenReturn(1000L);
        when(repository.getCreditLimit("ACC-1")).thenReturn(500L);

        var response = controller.getBalance("ACC-1");

        assertEquals("ACC-1", response.getAccountId());
        assertEquals(1000L, response.getBalance());
        assertEquals(500L, response.getCreditLimit());
    }
}