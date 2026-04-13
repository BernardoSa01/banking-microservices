package com.example.notification_service.model;

import java.time.LocalDateTime;

public class NotificationEvent {

    private String transactionId;
    private String accountId;
    private Long amount;
    private String type;
    private LocalDateTime timestamp;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public NotificationEvent() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
