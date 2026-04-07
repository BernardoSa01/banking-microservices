package com.example.workflow.delegate;

import com.example.workflow.dto.StatementRequestDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class RegisterStatementDelegate implements JavaDelegate {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void execute(DelegateExecution execution) {

        String transactionId = (String) execution.getVariable("transactionId");
        String accountId = (String) execution.getVariable("accountId");
        Long amount = (Long) execution.getVariable("amount");
        String type = (String) execution.getVariable("type");

        StatementRequestDTO request = new StatementRequestDTO();
        request.setTransactionId(transactionId);
        request.setAccountId(accountId);
        request.setAmount(amount);
        request.setType(type);
        request.setStatus("APPROVED");
        request.setTimestamp(LocalDateTime.now());

        String url = "http://localhost:8083/statements";

        try {
            System.out.println("Calling statement-service...");

            restTemplate.postForObject(url, request, Void.class);

            System.out.println("Statement registered for transaction: " + transactionId);
        } catch (Exception e) {

            System.err.println("Error calling statement-service: " + e.getMessage());

            throw new RuntimeException(
                    "Failed to register statement for transaction " + transactionId,
                    e
            );
        }
    }
}
