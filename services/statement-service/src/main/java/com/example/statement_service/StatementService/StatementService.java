package com.example.statement_service.StatementService;

import com.example.statement_service.dto.StatementRequestDTO;
import com.example.statement_service.model.Statement;
import com.example.statement_service.repository.StatementRepository;
import org.springframework.stereotype.Service;

@Service
public class StatementService {

    private final StatementRepository repository;

    public StatementService(StatementRepository repository) {
        this.repository = repository;
    }

    public Statement save(StatementRequestDTO dto) {

        Statement statement = Statement.builder()
                .transactionId(dto.getTransactionId())
                .accountId(dto.getAccountId())
                .amount(dto.getAmount())
                .type(dto.getType())
                .status(dto.getStatus())
                .timestamp(dto.getTimestamp())
                .build();

        return repository.save(statement);
    }
}
