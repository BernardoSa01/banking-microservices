package com.example.transaction_service.dto;

import com.example.transaction_service.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    @NotBlank
    private String accountId;

    @NotNull
    private TransactionType type;

    @NotNull
    @Positive
    private Long amount;
}
