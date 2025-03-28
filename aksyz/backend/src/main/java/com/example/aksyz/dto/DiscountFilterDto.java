package com.example.aksyz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountFilterDto {

    private String city;
    private String category;
    private String storeName;
    private Integer minDiscountPercent;
    private String search;
}