package com.example.notification_service.controller;

import com.example.notification_service.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Endpoint para abertura de terminal SSE
    @GetMapping("/stream")
    public SseEmitter stream(@RequestParam String accountId) {
        return service.subscribe(accountId);
    }
}
