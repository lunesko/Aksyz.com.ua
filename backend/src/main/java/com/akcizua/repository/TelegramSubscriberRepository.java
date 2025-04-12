package com.akcizua.repository;

import com.akcizua.model.TelegramSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramSubscriberRepository extends JpaRepository<TelegramSubscriber, Long> {

    Optional<TelegramSubscriber> findByChatId(Long chatId);
    
    List<TelegramSubscriber> findByActiveTrue();
    
    List<TelegramSubscriber> findByActiveTrueAndCity(String city);
    
    List<TelegramSubscriber> findByActiveTrueAndStore(String store);
    
    List<TelegramSubscriber> findByActiveTrueAndMinDiscountLessThanEqual(Integer discountPercentage);
}
