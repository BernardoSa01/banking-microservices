package com.example.transaction_service.service;

import com.example.transaction_service.dto.TransactionRequestDTO;
import com.example.transaction_service.dto.TransactionResponseDTO;
import com.example.transaction_service.exception.AccountNotFoundException;
import com.example.transaction_service.exception.CreditLimitExceededException;
import com.example.transaction_service.exception.InsufficientBalanceException;
import com.example.transaction_service.mapper.TransactionMapper;
import com.example.transaction_service.model.Transaction;
import com.example.transaction_service.model.TransactionStatus;
import com.example.transaction_service.model.TransactionType;
import com.example.transaction_service.repository.AccountBalanceRepository;
import org.springframework.stereotype.Service;

import com.example.transaction_service.kafka.TransactionEvent;
import com.example.transaction_service.kafka.TransactionProducer;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final AccountBalanceRepository repository;
    private final TransactionProducer transactionProducer;

    public TransactionService(TransactionMapper transactionMapper, AccountBalanceRepository repository, TransactionProducer transactionProducer) {
        this.transactionMapper = transactionMapper;
        this.repository = repository;
        this.transactionProducer = transactionProducer;
    }

    public TransactionResponseDTO processTransaction(TransactionRequestDTO dto) {

        if (!repository.accountExists(dto.getAccountId())) {
            throw new AccountNotFoundException(dto.getAccountId());
        }

        Transaction transaction;
        transaction = transactionMapper.toEntity(dto);

        transaction.setId(UUID.randomUUID().toString());
        transaction.setTimestamp(LocalDateTime.now());

        String message;

        if (transaction.getType() == TransactionType.DEBIT) {

            boolean success = repository.debit(transaction.getAccountId(), transaction.getAmount());

            if (success) {
                transaction.setStatus(TransactionStatus.APPROVED);
                message = "Transação a débito aprovada.";
            } else {
                transaction.setStatus(TransactionStatus.REJECTED);
                throw new InsufficientBalanceException(transaction.getAccountId());
            }

        } else if (transaction.getType() == TransactionType.CREDIT) {

            boolean success = repository.consumeCreditLimit(
                    transaction.getAccountId(),
                    transaction.getAmount()
            );

            if (success) {
                transaction.setStatus(TransactionStatus.APPROVED);
                message = "Transação a crédito aprovada.";
            } else {
                transaction.setStatus(TransactionStatus.REJECTED);
                throw new CreditLimitExceededException(transaction.getAccountId());
            }

        } else {
            transaction.setStatus(TransactionStatus.REJECTED);
            message = "Invalid transaction type";
        }

        TransactionResponseDTO response = transactionMapper.toDto(transaction);
        response.setMessage(message);

        if (transaction.getType() == TransactionType.DEBIT) {
            Long remainingBalance = repository.getBalance(transaction.getAccountId());
            response.setBalanceRemaining(remainingBalance);
        }

        if (transaction.getType() == TransactionType.CREDIT) {
            Long remainingLimit = repository.getCreditLimit(transaction.getAccountId());
            response.setCreditLimitRemaining(remainingLimit);
        }

        TransactionEvent event = new TransactionEvent(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getTimestamp()
        );

        transactionProducer.sendTransactionEvent(event);

        return response;
    }



//        if (transaction.getType() == TransactionType.DEBIT) {
//
//            Long balance = repository.getBalance(transaction.getAccountId());
//
//            if (balance >= transaction.getAmount()) {
//                repository.debit(transaction.getAccountId(), transaction.getAmount());
//                transaction.setStatus(TransactionStatus.APPROVED);
//                message = "Transação a débito aprovada.";
//            } else {
//                transaction.setStatus(TransactionStatus.REJECTED);
//                throw new InsufficientBalanceException(transaction.getAccountId());
//            }
//
//        } else if (transaction.getType() == TransactionType.CREDIT) {
//            Long limit = repository.getCreditLimit(transaction.getAccountId());
//
//            if (limit >= transaction.getAmount()) {
//                repository.consumeCreditLimit(transaction.getAccountId(), transaction.getAmount());
//                transaction.setStatus(TransactionStatus.APPROVED);
//                message = "Transação a crédito aprovada.";
//            } else {
//                transaction.setStatus(TransactionStatus.REJECTED);
//                throw new CreditLimitExceededException(transaction.getAccountId());
//            }
//        } else {
//            transaction.setStatus(TransactionStatus.REJECTED);
//            message = "Invalid transaction type";
//        }
//
//        TransactionResponseDTO response = transactionMapper.toDto(transaction);
//        response.setMessage(message);
//
//        if (transaction.getType() == TransactionType.DEBIT) {
//            Long remainingBalance = repository.getBalance(transaction.getAccountId());
//            response.setBalanceRemaining(remainingBalance);
//        }
//
//        if (transaction.getType() == TransactionType.CREDIT) {
//            Long remainingLimit = repository.getCreditLimit(transaction.getAccountId());
//            response.setCreditLimitRemaining(remainingLimit);
//        }
//
//        TransactionEvent event = new TransactionEvent(
//                transaction.getId(),
//                transaction.getAccountId(),
//                transaction.getAmount(),
//                transaction.getType(),
//                transaction.getStatus(),
//                transaction.getTimestamp()
//        );
//
//        transactionProducer.sendTransactionEvent(event);
//
//        return response;
//    }
}

