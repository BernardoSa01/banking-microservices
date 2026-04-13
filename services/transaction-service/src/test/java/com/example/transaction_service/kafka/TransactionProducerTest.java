package com.example.transaction_service.kafka;

import com.example.transaction_service.model.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

class TransactionProducerTest {

    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private TransactionProducer producer;

    @BeforeEach
    void setup() {

        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        producer = new TransactionProducer(kafkaTemplate);

        ReflectionTestUtils.setField(producer, "approvedTopic", "transactions-approved");
        ReflectionTestUtils.setField(producer, "rejectedTopic", "transactions-rejected");
    }

    @Test
    void shouldSendApprovedTransactionToApprovedTopic() {

        TransactionEvent event = new TransactionEvent();
        event.setStatus(TransactionStatus.APPROVED);

        producer.sendTransactionEvent(event);

        verify(kafkaTemplate).send("transactions-approved", event);
    }

    @Test
    void shouldSendRejectedTransactionToRejectedTopic() {

        TransactionEvent event = new TransactionEvent();
        event.setStatus(TransactionStatus.REJECTED);

        producer.sendTransactionEvent(event);

        verify(kafkaTemplate).send("transactions-rejected", event);
    }

    
}
