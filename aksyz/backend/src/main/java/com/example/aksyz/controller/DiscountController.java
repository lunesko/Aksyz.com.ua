package com.example.aksyz.controller;

import com.example.aksyz.dto.DiscountDto;
import com.example.aksyz.dto.DiscountFilterDto;
import com.example.aksyz.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Разрешаем CORS для всех источников (в продакшене нужно ограничить)
public class DiscountController {

    private final DiscountService discountService;

    /**
     * Получение списка скидок с фильтрацией
     */
    @GetMapping
    public ResponseEntity<Page<DiscountDto>> getDiscounts(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String store,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        DiscountFilterDto filterDto = DiscountFilterDto.builder()
                .city(city)
                .category(category)
                .storeName(store)
                .minDiscountPercent(minDiscount)
                .search(search)
                .build();

        Page<DiscountDto> discounts = discountService.getDiscounts(filterDto, page, size, sortBy, sortDir);
        return ResponseEntity.ok(discounts);
    }

    /**
     * Получение скидки по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Long id) {
        DiscountDto discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    /**
     * Создание новой скидки
     */
    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(@Valid @RequestBody DiscountDto discountDto) {
        DiscountDto createdDiscount = discountService.createDiscount(discountDto);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }

    /**
     * Обновление существующей скидки
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiscountDto> updateDiscount(
            @PathVariable Long id,
            @Valid @RequestBody DiscountDto discountDto) {
        DiscountDto updatedDiscount = discountService.updateDiscount(id, discountDto);
        return ResponseEntity.ok(updatedDiscount);
    }

    /**
     * Удаление скидки
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение списка доступных категорий
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = discountService.getCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Получение списка доступных городов
     */
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        List<String> cities = discountService.getCities();
        return ResponseEntity.ok(cities);
    }

    /**
     * Получение списка доступных магазинов
     */
    @GetMapping("/stores")
    public ResponseEntity<List<String>> getStores() {
        List<String> stores = discountService.getStores();
        return ResponseEntity.ok(stores);
    }
}