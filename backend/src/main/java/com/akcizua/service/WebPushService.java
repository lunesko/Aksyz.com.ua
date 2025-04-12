package com.akcizua.service;

import com.akcizua.dto.PushSubscriptionDto;
import com.akcizua.model.PushSubscription;
import com.akcizua.repository.PushSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Base64;

@Service
@Slf4j
public class WebPushService {

    private final PushSubscriptionRepository subscriptionRepository;
    
    @Value("${web.push.private.key}")
    private String privateKey;
    
    @Value("${web.push.public.key}")
    private String publicKey;

    @Autowired
    public WebPushService(PushSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Get the public VAPID key to be used by the client
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Save a new push subscription
     */
    @Transactional
    public PushSubscriptionDto saveSubscription(PushSubscriptionDto subscriptionDto) {
        PushSubscription subscription = convertToEntity(subscriptionDto);
        PushSubscription savedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(savedSubscription);
    }

    /**
     * Remove a push subscription
     */
    @Transactional
    public void removeSubscription(String endpoint) {
        subscriptionRepository.deleteByEndpoint(endpoint);
    }

    /**
     * Send a push notification to all subscribed clients
     */
    public void sendPushNotificationToAll(String title, String body, String icon, String tag) {
        List<PushSubscription> subscriptions = subscriptionRepository.findAll();
        
        for (PushSubscription subscription : subscriptions) {
            try {
                sendPushNotification(subscription, title, body, icon, tag);
            } catch (Exception e) {
                log.error("Failed to send push notification to {}: {}", subscription.getEndpoint(), e.getMessage(), e);
                
                // If the subscription is expired or invalid, we should remove it
                if (isSubscriptionExpired(e)) {
                    subscriptionRepository.delete(subscription);
                }
            }
        }
    }

    private boolean isSubscriptionExpired(Exception e) {
        // Check for specific error messages that indicate the subscription is expired
        return e.getMessage().contains("410") || e.getMessage().contains("404");
    }

    /**
     * Send a push notification to a specific client
     * In a real implementation, you would use a Web Push library like webpush-java
     */
    private void sendPushNotification(PushSubscription subscription, String title, String body, String icon, String tag) {
        // This is a simplified implementation.
        // In a real application, you would use a proper Web Push library
        // like webpush-java to send actual push notifications.
        
        log.info("Sending push notification to: {}", subscription.getEndpoint());
        log.info("Title: {}, Body: {}", title, body);
        
        // Here would be the actual implementation to send the push notification
        // using the Web Push Protocol
    }

    /**
     * Create authentication headers for Web Push (simplified)
     */
    private String createAuthenticationHeader(String audience) {
        try {
            // Create JWT header
            String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
                    "{\"typ\":\"JWT\",\"alg\":\"ES256\"}".getBytes());
            
            // Create JWT payload
            long expirationTime = System.currentTimeMillis() / 1000 + 12 * 60 * 60; // 12 hours
            String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                    String.format("{\"aud\":\"%s\",\"exp\":%d,\"sub\":\"mailto:contact@akcizua.com\"}", 
                            audience, expirationTime).getBytes());
            
            // Sign the JWT (simplified)
            String content = header + "." + payload;
            String signature = sign(content);
            
            return "vapid t=" + content + "." + signature + ", k=" + publicKey;
            
        } catch (Exception e) {
            log.error("Failed to create authentication header: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Sign the JWT (simplified)
     */
    private String sign(String content) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes(), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(content.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    /**
     * Convert DTO to Entity
     */
    private PushSubscription convertToEntity(PushSubscriptionDto dto) {
        return PushSubscription.builder()
                .endpoint(dto.getEndpoint())
                .auth(dto.getAuth())
                .p256dh(dto.getP256dh())
                .userAgent(dto.getUserAgent())
                .build();
    }

    /**
     * Convert Entity to DTO
     */
    private PushSubscriptionDto convertToDto(PushSubscription entity) {
        return PushSubscriptionDto.builder()
                .id(entity.getId())
                .endpoint(entity.getEndpoint())
                .auth(entity.getAuth())
                .p256dh(entity.getP256dh())
                .userAgent(entity.getUserAgent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
