package com.example.aksyz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String productName;

    private BigDecimal oldPrice;

    @NotNull(message = "New price is required")
    @Positive(message = "New price must be positive")
    private BigDecimal newPrice;

    private Integer discountPercent;

    @NotBlank(message = "Store name is required")
    private String storeName;

    private String city;

    private String category;

    private String link;

    private String imageUrl;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}