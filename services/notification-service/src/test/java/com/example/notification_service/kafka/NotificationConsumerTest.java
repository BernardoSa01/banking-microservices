package com.example.notification_service.kafka;

import com.example.notification_service.model.NotificationEvent;
import com.example.notification_service.service.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class NotificationConsumerTest {

    private NotificationService service;
    private NotificationConsumer consumer;

    @BeforeEach
    void setup() {
        service = Mockito.mock(NotificationService.class);
        consumer = new NotificationConsumer(service);
    }

    @Test
    void shouldConsumeKafkaEvent() {

        NotificationEvent event = new NotificationEvent();
        event.setAccountId("ACC-1");
        event.setAmount(100L);
        event.setType("DEBIT");
        event.setTimestamp(LocalDateTime.now());

        consumer.consume(event);

        verify(service).send("ACC-1", event);
    }
}
