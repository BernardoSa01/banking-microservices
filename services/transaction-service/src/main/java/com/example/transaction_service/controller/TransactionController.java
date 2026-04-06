package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BalanceResponseDTO;
import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.repository.AccountBalanceRepository;
import com.example.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final AccountBalanceRepository repository;

    public TransactionController(TransactionService transactionService, AccountBalanceRepository repository) {
        this.transactionService = transactionService;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> executeTransaction(@Valid @RequestBody TransactionRequestDTO dto) {

        TransactionResponseDTO transactionApproved = transactionService.processTransaction(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionApproved);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public BalanceResponseDTO getBalance(@PathVariable String accountId) {
        Long balance = repository.getBalance(accountId);
        Long limit = repository.getCreditLimit(accountId);

        return new BalanceResponseDTO(accountId, balance, limit);
    }
}
