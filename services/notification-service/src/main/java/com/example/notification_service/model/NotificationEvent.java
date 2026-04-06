package com.example.notification_service.model;

import java.time.LocalDateTime;

public class NotificationEvent {

    private String transactionId;
    private String accountId;
    private Long amount;
    private String type;
    private LocalDateTime timestamp;

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
