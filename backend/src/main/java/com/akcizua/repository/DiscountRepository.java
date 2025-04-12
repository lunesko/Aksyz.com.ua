package com.akcizua.repository;

import com.akcizua.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long>, JpaSpecificationExecutor<Discount> {

    List<Discount> findByCity(String city);
    
    List<Discount> findByStore(String store);
    
    List<Discount> findByDiscountPercentageGreaterThanEqual(Integer discountPercentage);
    
    List<Discount> findByCityAndStore(String city, String store);
    
    List<Discount> findByCityAndDiscountPercentageGreaterThanEqual(String city, Integer discountPercentage);
    
    List<Discount> findByStoreAndDiscountPercentageGreaterThanEqual(String store, Integer discountPercentage);
    
    List<Discount> findByCityAndStoreAndDiscountPercentageGreaterThanEqual(
            String city, String store, Integer discountPercentage);
    
    @Query("SELECT DISTINCT d.city FROM Discount d ORDER BY d.city")
    List<String> findAllCities();
    
    @Query("SELECT DISTINCT d.store FROM Discount d ORDER BY d.store")
    List<String> findAllStores();
    
    List<Discount> findByExpiresAtGreaterThan(LocalDateTime now);
    
    @Query("SELECT d FROM Discount d WHERE d.expiresAt > :now AND " +
           "(:city IS NULL OR d.city = :city) AND " +
           "(:store IS NULL OR d.store = :store) AND " +
           "(:minDiscount IS NULL OR d.discountPercentage >= :minDiscount)")
    List<Discount> findActiveDiscounts(
            @Param("now") LocalDateTime now,
            @Param("city") String city,
            @Param("store") String store,
            @Param("minDiscount") Integer minDiscount);
}
