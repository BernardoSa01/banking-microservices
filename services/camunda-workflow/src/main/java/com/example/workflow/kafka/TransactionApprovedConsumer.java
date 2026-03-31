package com.example.workflow.kafka;

import com.example.workflow.kafka.TransactionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionApprovedConsumer {

    private final RuntimeService runtimeService;
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    public TransactionApprovedConsumer(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @KafkaListener(topics = "transactions-approved", groupId = "camunda-workflow")
    public void consume(String message) throws Exception {

        TransactionEvent event = objectMapper.readValue(message, TransactionEvent.class);

        Map<String, Object> variables = new HashMap<>();
        variables.put("transactionId", event.getTransactionId());
        variables.put("accountId", event.getAccountId());
        variables.put("amount", event.getAmount());
        variables.put("type", event.getType());
        variables.put("timestamp", event.getTimestamp());

        System.out.println("Starting Camunda process for transaction: " + event.getTransactionId());
//        runtimeService.startProcessInstanceByKey(
//                "transaction-workflow-process",
//                variables
//        );

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                "transaction-workflow-process",
                variables
        );

        System.out.println("Process instance started: " + instance.getProcessInstanceId());
    }
}