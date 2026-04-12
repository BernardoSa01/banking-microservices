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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;

import com.example.transaction_service.kafka.TransactionEvent;
import com.example.transaction_service.kafka.TransactionProducer;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final AccountBalanceRepository repository;
    private final TransactionProducer transactionProducer;

    private final Counter transactionsTotal;
    private final Counter transactionsApproved;
    private final Counter transactionsRejected;
    private final Counter transactionAmountTotal;
    private final Timer transactionProcessingTimer;

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);


    public TransactionService(TransactionMapper transactionMapper,
                              AccountBalanceRepository repository,
                              TransactionProducer transactionProducer,
                              MeterRegistry meterRegistry) {

        this.transactionMapper = transactionMapper;
        this.repository = repository;
        this.transactionProducer = transactionProducer;

        this.transactionsTotal = Counter.builder("bank_transactions_total")
                .description("Total transactions processed")
                .register(meterRegistry);

        this.transactionsApproved = Counter.builder("bank_transactions_approved_total")
                .description("Total approved transactions")
                .register(meterRegistry);

        this.transactionsRejected = Counter.builder("bank_transactions_rejected_total")
                .description("Total rejected transactions")
                .register(meterRegistry);

        this.transactionAmountTotal = Counter.builder("bank_transaction_amount_total")
                .description("Total amount processed")
                .register(meterRegistry);

        this.transactionProcessingTimer = Timer.builder("transaction_processing_seconds")
                .description("Transaction processing time")
                .register(meterRegistry);
    }


    public TransactionResponseDTO processTransaction(TransactionRequestDTO dto) {


        Timer.Sample sample = Timer.start();

        transactionsTotal.increment();

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
                transactionsApproved.increment();
                transactionAmountTotal.increment(transaction.getAmount());
                message = "Transação a débito aprovada.";
                log.info(
                        "Transaction approved (DEBIT) - accountId={}, amount={}. Sending event to topic transactions-approved",
                        transaction.getAccountId(),
                        transaction.getAmount()
                );
            } else {
                transaction.setStatus(TransactionStatus.REJECTED);
                transactionsRejected.increment();
                log.info(
                        "Transaction rejected (DEBIT) - accountId={}, amount={}. Sending event to topic transactions-rejected",
                        transaction.getAccountId(),
                        transaction.getAmount()
                );
                throw new InsufficientBalanceException(transaction.getAccountId());
            }

        } else if (transaction.getType() == TransactionType.CREDIT) {

            boolean success = repository.consumeCreditLimit(
                    transaction.getAccountId(),
                    transaction.getAmount()
            );

            if (success) {
                transaction.setStatus(TransactionStatus.APPROVED);
                transactionsApproved.increment();
                transactionAmountTotal.increment(transaction.getAmount());
                message = "Transação a crédito aprovada.";
                log.info(
                        "Transaction approved (CREDIT) - accountId={}, amount={}. Sending event to topic transactions-approved",
                        transaction.getAccountId(),
                        transaction.getAmount()
                );
            } else {
                transaction.setStatus(TransactionStatus.REJECTED);
                transactionsRejected.increment();
                log.info(
                        "Transaction rejected (CREDIT) - accountId={}, amount={}. Sending event to topic transactions-rejected",
                        transaction.getAccountId(),
                        transaction.getAmount()
                );
                throw new CreditLimitExceededException(transaction.getAccountId());
            }

        } else {
            transaction.setStatus(TransactionStatus.REJECTED);
            transactionsRejected.increment();
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

        sample.stop(transactionProcessingTimer);

        return response;
    }
}

