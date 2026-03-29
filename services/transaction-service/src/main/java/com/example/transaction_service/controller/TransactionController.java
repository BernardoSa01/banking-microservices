package com.example.transaction_service.controller;

import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> executeTransaction(@Valid @RequestBody TransactionRequestDTO dto) {

        TransactionResponseDTO transactionApproved = transactionService.processTransaction(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionApproved);
    }
}
