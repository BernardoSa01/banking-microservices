package com.example.statement_service.controller;

import com.example.statement_service.StatementService.StatementPdfService;
import com.example.statement_service.StatementService.StatementService;
import com.example.statement_service.dto.StatementRequestDTO;
import com.example.statement_service.model.Statement;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/statements")
public class StatementController {

    private final StatementService service;
    private final StatementPdfService pdfService;

    public StatementController(StatementService service, StatementPdfService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<Statement> create(@RequestBody StatementRequestDTO dto) {

        Statement saved = service.save(dto);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Statement>> getStatementsByAccount(@RequestParam String accountId) {

        List<Statement> statements = service.getStatementsByAccount(accountId);

        return ResponseEntity.ok(statements);
    }

    @GetMapping("/period")
    public ResponseEntity<List<Statement>> getStatementsByTimestamp(
            @RequestParam String accountId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end)  {

        List<Statement> statements = service.getStatementsByPeriod(accountId, start, end);

        return ResponseEntity.ok(statements);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf(
            @RequestParam String accountId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
            ) {
        List<Statement> statements = service.getStatementsByPeriod(accountId, start, end);

        byte[] pdf = pdfService.generatePdf(accountId, start, end, statements);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=statement.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
