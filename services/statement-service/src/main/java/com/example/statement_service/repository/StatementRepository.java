package com.example.statement_service.repository;

import com.example.statement_service.model.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatementRepository extends MongoRepository<Statement, String> {

    List<Statement> findByAccountIdOrderByTimestampDesc(String accountId);

    List<Statement> findByAccountIdAndTimestampBetween(
            String accountId,
            LocalDateTime start,
            LocalDateTime end
    );
}
