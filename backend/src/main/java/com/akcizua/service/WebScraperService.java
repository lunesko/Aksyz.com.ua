package com.akcizua.service;

import com.akcizua.dto.DiscountDto;
import com.akcizua.model.Discount;
import com.akcizua.repository.DiscountRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WebScraperService {

    private final DiscountRepository discountRepository;
    private final DiscountService discountService;

    @Autowired
    public WebScraperService(DiscountRepository discountRepository, DiscountService discountService) {
        this.discountRepository = discountRepository;
        this.discountService = discountService;
    }

    /**
     * Scheduled method that runs every day at 3:00 AM to scrape discount websites
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    @CacheEvict(value = {"discountsCache", "citiesCache", "storesCache"}, allEntries = true)
    public void scheduledScraping() {
        log.info("Starting scheduled scraping of discount websites");
        
        List<DiscountDto> newDiscounts = new ArrayList<>();
        
        try {
            // Scrape discount websites
            newDiscounts.addAll(scrapeATB());
            // Add delay between scraping different websites to avoid being blocked
            TimeUnit.SECONDS.sleep(5);
            
            newDiscounts.addAll(scrapeSilpo());
            TimeUnit.SECONDS.sleep(5);
            
            newDiscounts.addAll(scrapeFora());
            
            // Save all new discounts
            for (DiscountDto discount : newDiscounts) {
                discountService.createDiscount(discount);
            }
            
            log.info("Completed scheduled scraping. Found {} new discounts", newDiscounts.size());
        } catch (Exception e) {
            log.error("Error during scheduled scraping: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Example method to scrape ATB website for discounts
     * Note: This is a simplified example and would need to be adapted to the actual website structure
     */
    private List<DiscountDto> scrapeATB() {
        log.info("Scraping ATB website for discounts");
        List<DiscountDto> discounts = new ArrayList<>();
        
        try {
            // In a real implementation, you would:
            // 1. Make a request to the ATB website
            // 2. Parse the HTML to find discount information
            // 3. Extract product names, prices, discounts, etc.
            
            // Example (not actual implementation):
            /*
            Document doc = Jsoup.connect("https://www.atbmarket.com/promotions").get();
            Elements discountItems = doc.select(".promotion-item");
            
            for (Element item : discountItems) {
                String productName = item.select(".product-name").text();
                String discountPriceText = item.select(".discount-price").text().replaceAll("[^\\d.]", "");
                String originalPriceText = item.select(".original-price").text().replaceAll("[^\\d.]", "");
                String imageUrl = item.select("img").attr("src");
                
                if (!productName.isEmpty() && !discountPriceText.isEmpty() && !originalPriceText.isEmpty()) {
                    BigDecimal discountPrice = new BigDecimal(discountPriceText);
                    BigDecimal originalPrice = new BigDecimal(originalPriceText);
                    
                    // Calculate discount percentage
                    BigDecimal difference = originalPrice.subtract(discountPrice);
                    int discountPercentage = difference.multiply(new BigDecimal(100))
                            .divide(originalPrice, 0, RoundingMode.HALF_UP)
                            .intValue();
                    
                    DiscountDto discount = DiscountDto.builder()
                            .productName(productName)
                            .discountPrice(discountPrice)
                            .originalPrice(originalPrice)
                            .store("АТБ")
                            .city("Киев") // You would need to handle multiple cities
                            .discountPercentage(discountPercentage)
                            .expiresAt(LocalDateTime.now().plusDays(7)) // Default expiration
                            .imageUrl(imageUrl)
                            .sourceUrl("https://www.atbmarket.com/promotions")
                            .build();
                    
                    discounts.add(discount);
                }
            }
            */
            
            log.info("Found {} discounts from ATB", discounts.size());
        } catch (Exception e) {
            log.error("Error scraping ATB website: {}", e.getMessage(), e);
        }
        
        return discounts;
    }
    
    /**
     * Example method to scrape Silpo website for discounts
     */
    private List<DiscountDto> scrapeSilpo() {
        log.info("Scraping Silpo website for discounts");
        List<DiscountDto> discounts = new ArrayList<>();
        
        // Similar implementation as scrapeATB but for Silpo website
        
        return discounts;
    }
    
    /**
     * Example method to scrape Fora website for discounts
     */
    private List<DiscountDto> scrapeFora() {
        log.info("Scraping Fora website for discounts");
        List<DiscountDto> discounts = new ArrayList<>();
        
        // Similar implementation as scrapeATB but for Fora website
        
        return discounts;
    }
}
