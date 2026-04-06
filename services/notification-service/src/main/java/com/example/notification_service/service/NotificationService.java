package com.example.notification_service.service;

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

    public void send(String accountId, Object event) {

        SseEmitter emitter = emitters.get(accountId);

        if (emitter == null) {
            System.out.println("User " + accountId + " offline.");
            return;
        }

        try {
            emitter.send(event);
        } catch (Exception e) {
            emitters.remove(accountId);
        }
    }
}
