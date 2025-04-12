package com.akcizua.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSubscriptionDto {

    private Long id;

    @NotBlank(message = "Endpoint is required")
    private String endpoint;

    @NotBlank(message = "Auth is required")
    private String auth;

    @NotBlank(message = "P256dh key is required")
    private String p256dh;

    private String userAgent;
    
    private LocalDateTime createdAt;
}
