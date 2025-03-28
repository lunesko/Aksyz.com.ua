package com.example.aksyz.repository;

import com.example.aksyz.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // Поиск скидок по городу
    Page<Discount> findByCity(String city, Pageable pageable);

    // Поиск скидок по категории
    Page<Discount> findByCategory(String category, Pageable pageable);

    // Поиск скидок по магазину
    Page<Discount> findByStoreName(String storeName, Pageable pageable);

    // Поиск по городу и категории
    Page<Discount> findByCityAndCategory(String city, String category, Pageable pageable);

    // Поиск с комбинированными фильтрами
    @Query("SELECT d FROM Discount d WHERE " +
           "(:city IS NULL OR d.city = :city) AND " +
           "(:category IS NULL OR d.category = :category) AND " +
           "(:storeName IS NULL OR d.storeName = :storeName) AND " +
           "(:minDiscountPercent IS NULL OR d.discountPercent >= :minDiscountPercent)")
    Page<Discount> findWithFilters(
            @Param("city") String city,
            @Param("category") String category,
            @Param("storeName") String storeName,
            @Param("minDiscountPercent") Integer minDiscountPercent,
            Pageable pageable);

    // Поиск по части названия товара (для реализации поисковой строки)
    Page<Discount> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    // Получение списка скидок, срок действия которых истек
    List<Discount> findByExpiresAtBefore(LocalDateTime dateTime);

    // Получение уникальных категорий
    @Query("SELECT DISTINCT d.category FROM Discount d WHERE d.category IS NOT NULL")
    List<String> findDistinctCategories();

    // Получение уникальных городов
    @Query("SELECT DISTINCT d.city FROM Discount d WHERE d.city IS NOT NULL")
    List<String> findDistinctCities();

    // Получение уникальных магазинов
    @Query("SELECT DISTINCT d.storeName FROM Discount d WHERE d.storeName IS NOT NULL")
    List<String> findDistinctStores();

    // Поиск по url страницы товара (для предотвращения дубликатов при парсинге)
    Optional<Discount> findByLink(String link);
}