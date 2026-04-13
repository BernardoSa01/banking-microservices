package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.model.Transaction;
import com.example.transaction_service.model.TransactionStatus;
import com.example.transaction_service.model.TransactionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper mapper = new TransactionMapper();

    @Test
    void shouldConvertRequestDtoToEntity() {

        TransactionRequestDTO dto = new TransactionRequestDTO(
                "ACC-1",
                TransactionType.DEBIT,
                100L
        );

        Transaction entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("ACC-1", entity.getAccountId());
        assertEquals(TransactionType.DEBIT, entity.getType());
        assertEquals(100L, entity.getAmount());
    }

    @Test
    void shouldConvertEntityToResponseDto() {

        Transaction entity = new Transaction();
        entity.setId("tx-123");
        entity.setAmount(200L);
        entity.setType(TransactionType.CREDIT);
        entity.setStatus(TransactionStatus.APPROVED);

        TransactionResponseDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("tx-123", dto.getTransactionId());
        assertEquals(TransactionStatus.APPROVED, dto.getStatus());
        assertEquals(TransactionType.CREDIT, dto.getType());
        assertEquals(200L, dto.getAmount());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }
}
