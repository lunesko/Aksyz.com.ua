package com.example.aksyz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "old_price")
    private BigDecimal oldPrice;

    @Column(name = "new_price", nullable = false)
    private BigDecimal newPrice;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @PrePersist
    @PreUpdate
    private void calculateDiscountPercent() {
        if (oldPrice != null && newPrice != null && oldPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diff = oldPrice.subtract(newPrice);
            BigDecimal percentage = diff.multiply(BigDecimal.valueOf(100)).divide(oldPrice, java.math.RoundingMode.HALF_UP);
            discountPercent = percentage.intValue();
        }
    }

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "city")
    private String city;

    @Column(name = "category")
    private String category;

    @Column(name = "link")
    private String link;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}