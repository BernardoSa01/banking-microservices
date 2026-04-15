package com.example.notification_service.kafka;

import com.example.notification_service.model.NotificationEvent;
import com.example.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notification-events", groupId = "notification-service")
    public void consume(NotificationEvent event) {

        //System.out.println("EVENT RECEIVED FROM KAFKA: " + event.getAccountId());

        log.info("Kafka event consumed | topic = notification-events | transactionId ={} | accountdId ={}",
                event.getTransactionId(),
                event.getAccountId());

        notificationService.send(event.getAccountId(), event);
    }
}
