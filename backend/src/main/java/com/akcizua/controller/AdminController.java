package com.akcizua.controller;

import com.akcizua.model.Discount;
import com.akcizua.model.TelegramSubscriber;
import com.akcizua.repository.DiscountRepository;
import com.akcizua.repository.TelegramSubscriberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Admin API", description = "Управление подписчиками и скидками")
public class AdminController {

    @Autowired
    private TelegramSubscriberRepository subscriberRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping("/admin/subscribers")
    @Operation(summary = "Получить всех Telegram-подписчиков")
    public List<TelegramSubscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    @DeleteMapping("/admin/subscribers/{chatId}")
    @Operation(summary = "Удалить подписчика по chatId")
    public ResponseEntity<?> deleteSubscriber(@PathVariable Long chatId) {
        subscriberRepository.deleteById(chatId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/discounts")
    @Operation(summary = "Получить все скидки")
    public List<Discount> getAllDiscounts(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String store,
            @RequestParam(required = false) Integer minDiscount
    ) {
        return discountRepository.findAll().stream()
                .filter(d -> city == null || d.getCity().equalsIgnoreCase(city))
                .filter(d -> store == null || d.getStore().equalsIgnoreCase(store))
                .filter(d -> minDiscount == null || d.getDiscountPercentage() >= minDiscount)
                .toList();
    }

    @PostMapping("/admin/discounts")
    @Operation(summary = "Добавить новую скидку")
    public Discount addDiscount(@RequestBody Discount discount) {
        return discountRepository.save(discount);
    }

    @DeleteMapping("/admin/discounts/{id}")
    @Operation(summary = "Удалить скидку по ID")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {
            discountRepository.delete(discount.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
