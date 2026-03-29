package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDTO dto) {
        if (dto == null) return null;

        Transaction transaction = new Transaction();
        transaction.setAccountId(dto.getAccountId());
        transaction.setType(dto.getType());
        transaction.setAmount(dto.getAmount());

        return transaction;
    }

    public TransactionResponseDTO toDto(Transaction entity) {
        if (entity == null) return null;

        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());

        return dto;
    }
}
