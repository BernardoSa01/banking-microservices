package com.example.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BalanceResponseDTO {

    private String accountId;
    private Long balance;
    private Long creditLimit;
}
