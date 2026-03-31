package com.example.workflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatementRequestDTO {

    private String transactionId;
    private String accountId;
    private Long amount;
    private String type;
    private String status;
    private LocalDateTime timestamp;
}
