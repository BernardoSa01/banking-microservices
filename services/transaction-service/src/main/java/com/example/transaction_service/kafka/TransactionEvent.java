package com.example.transaction_service.kafka;

import com.example.transaction_service.model.TransactionStatus;
import com.example.transaction_service.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TransactionEvent {

    private String transactionId;
    private String accountId;
    private Long amount;
    private TransactionType type;
    private TransactionStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public TransactionEvent() {}

    public TransactionEvent(String transactionId, String accountId, Long amount, TransactionType type, TransactionStatus status, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
