package com.akcizua.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "discount_price", nullable = false)
    private BigDecimal discountPrice;

    @Column(name = "original_price", nullable = false)
    private BigDecimal originalPrice;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private String city;

    @Column(name = "discount_percentage", nullable = false)
    private Integer discountPercentage;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static Discount createSample(String productName, BigDecimal discountPrice, 
                                        BigDecimal originalPrice, String store, String city, 
                                        int discountPercentage, LocalDateTime expiresAt) {
        return Discount.builder()
                .productName(productName)
                .discountPrice(discountPrice)
                .originalPrice(originalPrice)
                .store(store)
                .city(city)
                .discountPercentage(discountPercentage)
                .expiresAt(expiresAt)
                .build();
    }
}
