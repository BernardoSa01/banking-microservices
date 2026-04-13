package com.example.statement_service.service;

import com.example.statement_service.StatementService.StatementPdfService;
import com.example.statement_service.model.Statement;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatementPdfServiceTest {

    private final StatementPdfService service = new StatementPdfService();

    @Test
    void shouldGeneratePdf() {

        Statement statement = Statement.builder()
                .accountId("ACC-1")
                .transactionId("tx-1")
                .amount(100L)
                .type("DEBIT")
                .status("APPROVED")
                .timestamp(LocalDateTime.now())
                .build();

        List<Statement> statements = new ArrayList<>(List.of(statement));

        byte[] pdf = service.generatePdf(
                "ACC-1",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                statements
        );

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}