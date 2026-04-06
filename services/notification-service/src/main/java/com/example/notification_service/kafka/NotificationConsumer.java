package com.example.notification_service.kafka;

import com.example.notification_service.model.NotificationEvent;
import com.example.notification_service.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notification-events", groupId = "notification-service")
    public void consume(NotificationEvent event) {

        System.out.println("EVENT RECEIVED FROM KAFKA: " + event.getAccountId());

        notificationService.send(event.getAccountId(), event);
    }
}
