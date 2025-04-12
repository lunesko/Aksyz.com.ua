package com.akcizua.dto;

import com.akcizua.model.Discount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull(message = "Discount price is required")
    @PositiveOrZero(message = "Discount price must be positive or zero")
    private BigDecimal discountPrice;

    @NotNull(message = "Original price is required")
    @Positive(message = "Original price must be positive")
    private BigDecimal originalPrice;

    @NotBlank(message = "Store is required")
    private String store;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Discount percentage is required")
    @PositiveOrZero(message = "Discount percentage must be positive or zero")
    private Integer discountPercentage;

    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiresAt;

    private String imageUrl;
    private String sourceUrl;
    private LocalDateTime createdAt;

    // Convert DTO to Entity
    public Discount toEntity() {
        return Discount.builder()
                .id(id)
                .productName(productName)
                .discountPrice(discountPrice)
                .originalPrice(originalPrice)
                .store(store)
                .city(city)
                .discountPercentage(discountPercentage)
                .expiresAt(expiresAt)
                .imageUrl(imageUrl)
                .sourceUrl(sourceUrl)
                .createdAt(createdAt)
                .build();
    }

    // Convert Entity to DTO
    public static DiscountDto fromEntity(Discount discount) {
        return DiscountDto.builder()
                .id(discount.getId())
                .productName(discount.getProductName())
                .discountPrice(discount.getDiscountPrice())
                .originalPrice(discount.getOriginalPrice())
                .store(discount.getStore())
                .city(discount.getCity())
                .discountPercentage(discount.getDiscountPercentage())
                .expiresAt(discount.getExpiresAt())
                .imageUrl(discount.getImageUrl())
                .sourceUrl(discount.getSourceUrl())
                .createdAt(discount.getCreatedAt())
                .build();
    }
}
