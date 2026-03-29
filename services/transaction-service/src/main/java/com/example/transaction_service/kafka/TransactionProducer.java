package com.example.transaction_service.kafka;

import com.example.transaction_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    @Value("${kafka.topics.approved}")
    private String approvedTopic;

    @Value("${kafka.topics.rejected}")
    private String rejectedTopic;

    public TransactionProducer(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionEvent(TransactionEvent event) {

        String topic;

        if (event.getStatus() == TransactionStatus.APPROVED) {
            topic = approvedTopic;
        } else {
            topic = rejectedTopic;
        }

        kafkaTemplate.send(topic, event);
    }

}
