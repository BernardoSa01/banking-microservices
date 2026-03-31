package com.example.statement_service.controller;

import com.example.statement_service.StatementService.StatementService;
import com.example.statement_service.dto.StatementRequestDTO;
import com.example.statement_service.model.Statement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/statements")
public class StatementController {

    private final StatementService service;

    public StatementController(StatementService service) {
        this.service = service;
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
}
