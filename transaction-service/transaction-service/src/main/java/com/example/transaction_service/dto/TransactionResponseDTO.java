package com.example.transaction_service.dto;

import com.example.transaction_service.model.TransactionStatus;
import com.example.transaction_service.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private String transactionId;
    private TransactionStatus status;
    private TransactionType type;
    private Long amount;
    private Long balanceRemaining;
    private Long creditLimitRemaining;
    private String message;
}
