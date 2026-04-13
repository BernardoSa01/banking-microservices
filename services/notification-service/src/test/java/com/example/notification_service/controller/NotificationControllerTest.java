package com.example.notification_service.controller;

import com.example.notification_service.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    private NotificationService service;
    private NotificationController controller;

    @BeforeEach
    void setup() {
        service = Mockito.mock(NotificationService.class);
        controller = new NotificationController(service);
    }

    @Test
    void shouldReturnSseEmitter() {

        SseEmitter emitter = new SseEmitter();

        when(service.subscribe("ACC-1")).thenReturn(emitter);

        SseEmitter result = controller.stream("ACC-1");

        assertNotNull(result);
        verify(service).subscribe("ACC-1");
    }
}
