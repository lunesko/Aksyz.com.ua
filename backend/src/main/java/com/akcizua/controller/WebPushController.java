package com.akcizua.controller;

import com.akcizua.dto.PushSubscriptionDto;
import com.akcizua.service.WebPushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/push")
@Tag(name = "Web Push", description = "API для управления Web Push уведомлениями")
@CrossOrigin(origins = "*") // For development; restrict in production
public class WebPushController {

    private final WebPushService webPushService;

    @Autowired
    public WebPushController(WebPushService webPushService) {
        this.webPushService = webPushService;
    }

    @GetMapping("/vapid-public-key")
    @Operation(summary = "Получить публичный VAPID ключ", description = "Возвращает публичный VAPID ключ для инициализации Web Push на клиенте")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        String publicKey = webPushService.getPublicKey();
        return ResponseEntity.ok(Map.of("publicKey", publicKey));
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Подписаться на уведомления", description = "Регистрирует новую подписку на Web Push уведомления")
    public ResponseEntity<PushSubscriptionDto> subscribe(@Valid @RequestBody PushSubscriptionDto subscription) {
        PushSubscriptionDto savedSubscription = webPushService.saveSubscription(subscription);
        return ResponseEntity.ok(savedSubscription);
    }

    @DeleteMapping("/unsubscribe")
    @Operation(summary = "Отписаться от уведомлений", description = "Удаляет подписку на Web Push уведомления")
    public ResponseEntity<Void> unsubscribe(@RequestParam String endpoint) {
        webPushService.removeSubscription(endpoint);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test")
    @Operation(summary = "Тестовое уведомление", description = "Отправляет тестовое уведомление всем подписчикам")
    public ResponseEntity<Map<String, String>> sendTestNotification() {
        webPushService.sendPushNotificationToAll(
                "АкцизUA - Тестовое уведомление",
                "Это тестовое уведомление. Ваша подписка работает корректно!",
                "/images/logo.png",
                "test-notification"
        );
        return ResponseEntity.ok(Map.of("message", "Тестовое уведомление отправлено"));
    }
}
