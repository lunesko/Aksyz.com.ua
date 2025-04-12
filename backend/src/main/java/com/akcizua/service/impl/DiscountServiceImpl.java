package com.akcizua.service.impl;

import com.akcizua.dto.DiscountDto;
import com.akcizua.model.Discount;
import com.akcizua.repository.DiscountRepository;
import com.akcizua.service.DiscountService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    @Cacheable(value = "discountsCache", key = "'discounts_' + #city + '_' + #store + '_' + #minDiscount")
    public List<DiscountDto> getDiscounts(String city, String store, Integer minDiscount) {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> discounts = discountRepository.findActiveDiscounts(now, city, store, minDiscount);
        return discounts.stream()
                .map(DiscountDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "citiesCache")
    public List<String> getAllCities() {
        return discountRepository.findAllCities();
    }

    @Override
    @Cacheable(value = "storesCache")
    public List<String> getAllStores() {
        return discountRepository.findAllStores();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"discountsCache", "citiesCache", "storesCache"}, allEntries = true)
    public DiscountDto createDiscount(DiscountDto discountDto) {
        Discount discount = discountDto.toEntity();
        Discount savedDiscount = discountRepository.save(discount);
        return DiscountDto.fromEntity(savedDiscount);
    }

    @Override
    public DiscountDto getDiscountById(Long id) {
        return discountRepository.findById(id)
                .map(DiscountDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Discount not found with id: " + id));
    }

    @Override
    @PostConstruct
    @Transactional
    public void initSampleData() {
        // Only initialize if the repository is empty
        if (discountRepository.count() == 0) {
            // Sample 1: Vodka discount in Kyiv
            Discount sample1 = Discount.createSample(
                    "Vodka Nemiroff De Luxe 0.5L",
                    new BigDecimal("149.99"),
                    new BigDecimal("219.99"),
                    "АТБ",
                    "Киев",
                    32,
                    LocalDateTime.now().plusDays(7)
            );
            
            // Sample 2: Whiskey discount in Lviv
            Discount sample2 = Discount.createSample(
                    "Whiskey Jack Daniel's 0.7L",
                    new BigDecimal("599.99"),
                    new BigDecimal("799.99"),
                    "Сильпо",
                    "Львов",
                    25,
                    LocalDateTime.now().plusDays(14)
            );
            
            // Sample 3: Cigarettes discount in Odessa
            Discount sample3 = Discount.createSample(
                    "Cigarettes Marlboro Red 1 pack",
                    new BigDecimal("69.99"),
                    new BigDecimal("89.99"),
                    "Фора",
                    "Одесса",
                    22,
                    LocalDateTime.now().plusDays(5)
            );
            
            discountRepository.saveAll(List.of(sample1, sample2, sample3));
        }
    }
}
