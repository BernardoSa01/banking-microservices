package com.example.workflow.delegate;

import com.example.workflow.kafka.NotificationEvent;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PublishNotificationEventDelegate implements JavaDelegate {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PublishNotificationEventDelegate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void execute(DelegateExecution execution) {

        String transactionId = (String) execution.getVariable("transactionId");
        String accountId = (String) execution.getVariable("accountId");
        Long amount = (Long) execution.getVariable("amount");
        String type = (String) execution.getVariable("type");

        NotificationEvent event = new NotificationEvent();
        event.setTransactionId(transactionId);
        event.setAccountId(accountId);
        event.setAmount(amount);
        event.setType(type);
        event.setTimestamp(LocalDateTime.parse(LocalDateTime.now().toString()));

        kafkaTemplate.send("notification-events", event);


        System.out.println("Notification event published for account: " + accountId);
    }
}
