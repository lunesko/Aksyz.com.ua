package com.akcizua.controller;

import com.akcizua.dto.DiscountDto;
import com.akcizua.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discounts")
@Tag(name = "Discounts", description = "API для управления скидками")
@CrossOrigin(origins = "*") // For development; restrict in production
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    @Operation(summary = "Получить список скидок", description = "Возвращает все скидки с возможностью фильтрации по городу, магазину и минимальному проценту скидки")
    public ResponseEntity<List<DiscountDto>> getDiscounts(
            @Parameter(description = "Город") @RequestParam(required = false) String city,
            @Parameter(description = "Магазин") @RequestParam(required = false) String store,
            @Parameter(description = "Минимальный процент скидки") @RequestParam(required = false) Integer minDiscount) {
        List<DiscountDto> discounts = discountService.getDiscounts(city, store, minDiscount);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить скидку по ID", description = "Возвращает скидку по указанному ID")
    public ResponseEntity<DiscountDto> getDiscountById(
            @Parameter(description = "ID скидки") @PathVariable Long id) {
        DiscountDto discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    @PostMapping
    @Operation(summary = "Создать новую скидку", description = "Создает новую скидку (требуется аутентификация админа)")
    public ResponseEntity<DiscountDto> createDiscount(
            @Parameter(description = "Данные скидки") @Valid @RequestBody DiscountDto discountDto) {
        DiscountDto createdDiscount = discountService.createDiscount(discountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
    }

    @GetMapping("/filters")
    @Operation(summary = "Получить доступные фильтры", description = "Возвращает список доступных городов и магазинов для фильтрации")
    public ResponseEntity<Map<String, List<String>>> getFilters() {
        List<String> cities = discountService.getAllCities();
        List<String> stores = discountService.getAllStores();
        
        Map<String, List<String>> filters = Map.of(
                "cities", cities,
                "stores", stores
        );
        
        return ResponseEntity.ok(filters);
    }
}
