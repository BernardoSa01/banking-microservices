package com.example.notification_service.service;

import com.example.notification_service.model.NotificationEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String accountId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(accountId, emitter);

        emitter.onCompletion(() -> emitters.remove(accountId));
        emitter.onTimeout(() -> emitters.remove(accountId));

        return emitter;
    }

    public void send(String accountId, NotificationEvent event) {

        SseEmitter emitter = emitters.get(accountId);

        if (emitter == null) {
            System.out.println("User " + accountId + " offline.");
            return;
        }

        // Converte TransactionType (DEBIT/CREDIT) para português (exibição)
        String type = event.getType().equals("DEBIT") ? "débito" : "crédito";

        String message = String.format(
                " Conta %s: Compra aprovada no %s no valor de R$ %,.2f",
                accountId,
                type,
                event.getAmount().doubleValue()
        );

        try {
            //emitter.send(message);
            emitter.send(
                    SseEmitter.event()
                            .name("notification")
                            .data(message)
            );
        } catch (Exception e) {
            emitters.remove(accountId);
        }
    }
}
