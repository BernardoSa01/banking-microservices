package com.example.statement_service.service;

import com.example.statement_service.StatementService.StatementService;
import com.example.statement_service.dto.StatementRequestDTO;
import com.example.statement_service.model.Statement;
import com.example.statement_service.repository.StatementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatementServiceTest {

    private StatementRepository repository;
    private StatementService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(StatementRepository.class);
        service = new StatementService(repository);
    }

    @Test
    void shouldSaveStatement() {

        StatementRequestDTO dto = new StatementRequestDTO();
        dto.setTransactionId("tx-1");
        dto.setAccountId("ACC-1");
        dto.setAmount(100L);
        dto.setType("DEBIT");
        dto.setStatus("APPROVED");
        dto.setTimestamp(LocalDateTime.now());

        Statement statement = Statement.builder()
                .transactionId(dto.getTransactionId())
                .accountId(dto.getAccountId())
                .amount(dto.getAmount())
                .type(dto.getType())
                .status(dto.getStatus())
                .timestamp(dto.getTimestamp())
                .build();

        when(repository.save(any())).thenReturn(statement);

        Statement result = service.save(dto);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void shouldReturnStatementsByAccount() {

        Statement statement = new Statement();

        when(repository.findByAccountIdOrderByTimestampDesc("ACC-1"))
                .thenReturn(List.of(statement));

        List<Statement> result = service.getStatementsByAccount("ACC-1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnStatementsByPeriod() {

        Statement statement = new Statement();

        when(repository.findByAccountIdAndTimestampBetween(any(), any(), any()))
                .thenReturn(List.of(statement));

        List<Statement> result = service.getStatementsByPeriod(
                "ACC-1",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        assertEquals(1, result.size());
    }
}