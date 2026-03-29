package com.example.transaction_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String id;
    private String accountId;
    private Long amount;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime timestamp;
}
