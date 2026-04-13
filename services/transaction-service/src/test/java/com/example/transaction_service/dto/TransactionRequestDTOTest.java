package com.example.transaction_service.dto;

import com.example.transaction_service.model.TransactionType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TransactionRequestDTOTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldFailWhenAmountIsNegative() {

        TransactionRequestDTO dto =
                new TransactionRequestDTO("ACC-1", TransactionType.DEBIT, -10L);

        Set<ConstraintViolation<TransactionRequestDTO>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }
}
