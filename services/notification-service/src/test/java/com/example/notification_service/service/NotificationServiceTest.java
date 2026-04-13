package com.example.notification_service.service;

import com.example.notification_service.model.NotificationEvent;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    private NotificationService service;

    @BeforeEach
    void setup() {
        service = new NotificationService(new SimpleMeterRegistry());
    }

    @Test
    void shouldSubscribeEmitter() {

        SseEmitter emitter = service.subscribe("ACC-1");

        assertNotNull(emitter);
    }

    @Test
    void shouldSendNotificationWhenEmitterExists() {

        service.subscribe("ACC-1");

        NotificationEvent event = new NotificationEvent();
        event.setAccountId("ACC-1");
        event.setAmount(100L);
        event.setType("DEBIT");
        event.setTimestamp(LocalDateTime.now());

        assertDoesNotThrow(() ->
                service.send("ACC-1", event)
        );
    }

    @Test
    void shouldIgnoreWhenUserOffline() {

        NotificationEvent event = new NotificationEvent();
        event.setAccountId("ACC-1");
        event.setAmount(100L);
        event.setType("DEBIT");
        event.setTimestamp(LocalDateTime.now());

        assertDoesNotThrow(() ->
                service.send("ACC-1", event)
        );
    }
}
