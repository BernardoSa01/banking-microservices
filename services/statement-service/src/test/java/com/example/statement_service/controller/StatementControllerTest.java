package com.example.statement_service.controller;

import com.example.statement_service.StatementService.StatementPdfService;
import com.example.statement_service.StatementService.StatementService;
import com.example.statement_service.dto.StatementRequestDTO;
import com.example.statement_service.model.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatementControllerTest {

    private StatementService service;
    private StatementPdfService pdfService;

    private StatementController controller;

    @BeforeEach
    void setup() {
        service = Mockito.mock(StatementService.class);
        pdfService = Mockito.mock(StatementPdfService.class);
        controller = new StatementController(service, pdfService);
    }

    @Test
    void shouldCreateStatement() {

        StatementRequestDTO dto = new StatementRequestDTO();
        Statement statement = new Statement();

        when(service.save(dto)).thenReturn(statement);

        var response = controller.create(dto);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnStatementsByAccount() {

        when(service.getStatementsByAccount("ACC-1"))
                .thenReturn(List.of(new Statement()));

        var response = controller.getStatementsByAccount("ACC-1");

        assertEquals(1, response.getBody().size());
    }
}
