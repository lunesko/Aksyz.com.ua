package com.example.aksyz.service;

import com.example.aksyz.dto.DiscountDto;
import com.example.aksyz.dto.DiscountFilterDto;
import com.example.aksyz.exception.ResourceNotFoundException;
import com.example.aksyz.model.Discount;
import com.example.aksyz.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountService {

    private final DiscountRepository discountRepository;

    /**
     * Получение списка скидок с фильтрацией
     */
    @Transactional(readOnly = true)
    public Page<DiscountDto> getDiscounts(DiscountFilterDto filterDto, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Discount> discountsPage = discountRepository.findWithFilters(
                filterDto.getCity(),
                filterDto.getCategory(),
                filterDto.getStoreName(),
                filterDto.getMinDiscountPercent(),
                pageable);

        return discountsPage.map(this::convertToDto);
    }

    /**
     * Получение скидки по ID
     */
    @Transactional(readOnly = true)
    public DiscountDto getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
        return convertToDto(discount);
    }

    /**
     * Создание новой скидки
     */
    @Transactional
    public DiscountDto createDiscount(DiscountDto discountDto) {
        Discount discount = convertToEntity(discountDto);
        
        // Проверка на дубликаты, если есть ссылка
        if (discount.getLink() != null && !discount.getLink().isEmpty()) {
            discountRepository.findByLink(discount.getLink())
                    .ifPresent(existingDiscount -> {
                        throw new IllegalArgumentException("Discount with this link already exists");
                    });
        }
        
        Discount savedDiscount = discountRepository.save(discount);
        log.info("Created new discount: {}", savedDiscount.getProductName());
        return convertToDto(savedDiscount);
    }

    /**
     * Обновление существующей скидки
     */
    @Transactional
    public DiscountDto updateDiscount(Long id, DiscountDto discountDto) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));

        // Обновляем поля
        existingDiscount.setProductName(discountDto.getProductName());
        existingDiscount.setOldPrice(discountDto.getOldPrice());
        existingDiscount.setNewPrice(discountDto.getNewPrice());
        existingDiscount.setStoreName(discountDto.getStoreName());
        existingDiscount.setCity(discountDto.getCity());
        existingDiscount.setCategory(discountDto.getCategory());
        existingDiscount.setLink(discountDto.getLink());
        existingDiscount.setImageUrl(discountDto.getImageUrl());
        existingDiscount.setExpiresAt(discountDto.getExpiresAt());

        Discount updatedDiscount = discountRepository.save(existingDiscount);
        log.info("Updated discount with id: {}", id);
        return convertToDto(updatedDiscount);
    }

    /**
     * Удаление скидки
     */
    @Transactional
    public void deleteDiscount(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Discount not found with id: " + id);
        }
        discountRepository.deleteById(id);
        log.info("Deleted discount with id: {}", id);
    }

    /**
     * Удаление устаревших скидок (запускается по расписанию)
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * ?") // Выполняется каждый день в 1:00
    public void deleteExpiredDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> expiredDiscounts = discountRepository.findByExpiresAtBefore(now);
        
        if (!expiredDiscounts.isEmpty()) {
            discountRepository.deleteAll(expiredDiscounts);
            log.info("Deleted {} expired discounts", expiredDiscounts.size());
        }
    }

    /**
     * Получение списка уникальных категорий
     */
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return discountRepository.findDistinctCategories();
    }

    /**
     * Получение списка уникальных городов
     */
    @Transactional(readOnly = true)
    public List<String> getCities() {
        return discountRepository.findDistinctCities();
    }

    /**
     * Получение списка уникальных магазинов
     */
    @Transactional(readOnly = true)
    public List<String> getStores() {
        return discountRepository.findDistinctStores();
    }

    /**
     * Конвертация из Entity в DTO
     */
    private DiscountDto convertToDto(Discount discount) {
        return DiscountDto.builder()
                .id(discount.getId())
                .productName(discount.getProductName())
                .oldPrice(discount.getOldPrice())
                .newPrice(discount.getNewPrice())
                .discountPercent(discount.getDiscountPercent())
                .storeName(discount.getStoreName())
                .city(discount.getCity())
                .category(discount.getCategory())
                .link(discount.getLink())
                .imageUrl(discount.getImageUrl())
                .expiresAt(discount.getExpiresAt())
                .createdAt(discount.getCreatedAt())
                .build();
    }

    /**
     * Конвертация из DTO в Entity
     */
    private Discount convertToEntity(DiscountDto dto) {
        return Discount.builder()
                .productName(dto.getProductName())
                .oldPrice(dto.getOldPrice())
                .newPrice(dto.getNewPrice())
                .storeName(dto.getStoreName())
                .city(dto.getCity())
                .category(dto.getCategory())
                .link(dto.getLink())
                .imageUrl(dto.getImageUrl())
                .expiresAt(dto.getExpiresAt())
                .build();
    }
}