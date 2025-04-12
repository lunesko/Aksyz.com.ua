package com.akcizua.service;

import com.akcizua.dto.DiscountDto;

import java.util.List;

public interface DiscountService {

    /**
     * Get all discounts with optional filtering
     *
     * @param city            Optional city filter
     * @param store           Optional store filter
     * @param minDiscount     Optional minimum discount percentage filter
     * @return List of discount DTOs
     */
    List<DiscountDto> getDiscounts(String city, String store, Integer minDiscount);

    /**
     * Get all available cities
     *
     * @return List of cities
     */
    List<String> getAllCities();

    /**
     * Get all available stores
     *
     * @return List of stores
     */
    List<String> getAllStores();

    /**
     * Create a new discount
     *
     * @param discountDto The discount to create
     * @return The created discount DTO
     */
    DiscountDto createDiscount(DiscountDto discountDto);

    /**
     * Get a discount by ID
     *
     * @param id The discount ID
     * @return The discount DTO
     */
    DiscountDto getDiscountById(Long id);

    /**
     * Initialize sample data
     */
    void initSampleData();
}
