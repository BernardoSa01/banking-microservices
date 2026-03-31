package com.example.statement_service.controller;

import com.example.statement_service.StatementService.StatementService;
import com.example.statement_service.dto.StatementRequestDTO;
import com.example.statement_service.model.Statement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
