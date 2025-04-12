package com.akcizua.service;

import com.akcizua.dto.DiscountDto;
import com.akcizua.model.TelegramSubscriber;
import com.akcizua.repository.TelegramSubscriberRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramSubscriberRepository subscriberRepository;
    private final DiscountService discountService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public TelegramBotService(TelegramSubscriberRepository subscriberRepository, DiscountService discountService) {
        this.subscriberRepository = subscriberRepository;
        this.discountService = discountService;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("Telegram bot registered successfully");
        } catch (TelegramApiException e) {
            log.error("Error registering Telegram bot: {}", e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

            try {
                if (text.startsWith("/start")) {
                    sendWelcomeMessage(chatId);
                } else if (text.startsWith("/subscribe")) {
                    handleSubscribe(message);
                } else if (text.startsWith("/unsubscribe")) {
                    handleUnsubscribe(message);
                } else if (text.startsWith("/discounts")) {
                    handleDiscountsCommand(message);
                } else if (text.startsWith("/help")) {
                    sendHelpMessage(chatId);
                } else {
                    handleGenericMessage(message);
                }
            } catch (Exception e) {
                log.error("Error processing message from {}: {}", chatId, e.getMessage(), e);
                try {
                    execute(SendMessage.builder()
                            .chatId(chatId)
                            .text("Произошла ошибка при обработке вашего запроса. Пожалуйста, попробуйте позже.")
                            .build());
                } catch (TelegramApiException ex) {
                    log.error("Error sending error message to {}: {}", chatId, ex.getMessage(), ex);
                }
            }
        }
    }

    private void sendWelcomeMessage(String chatId) throws TelegramApiException {
        String welcomeText = "Добро пожаловать в АкцизUA - ваш персональный агрегатор скидок на алкоголь и табачную продукцию! 🎉\n\n" +
                "Команды:\n" +
                "/subscribe - подписаться на уведомления о скидках\n" +
                "/unsubscribe - отписаться от уведомлений\n" +
                "/discounts - показать текущие скидки\n" +
                "/help - показать справку\n\n" +
                "Используйте /subscribe, чтобы начать получать уведомления о лучших скидках!";

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(welcomeText)
                .build();

        execute(message);
    }

    private void sendHelpMessage(String chatId) throws TelegramApiException {
        String helpText = "АкцизUA - команды:\n\n" +
                "/subscribe - подписаться на уведомления о скидках\n" +
                "Вы можете указать фильтры: /subscribe город магазин минимальный_процент_скидки\n" +
                "Например: /subscribe Киев АТБ 20\n\n" +
                "/unsubscribe - отписаться от уведомлений\n\n" +
                "/discounts - показать текущие скидки\n" +
                "Вы можете указать фильтры: /discounts город магазин минимальный_процент_скидки\n" +
                "Например: /discounts Киев АТБ 20\n\n" +
                "/help - показать эту справку";

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(helpText)
                .build();

        execute(message);
    }

    @Transactional
    private void handleSubscribe(Message message) throws TelegramApiException {
        String chatId = message.getChatId().toString();
        User user = message.getFrom();
        String text = message.getText();
        
        // Parse command arguments
        String[] args = text.split("\\s+");
        String city = args.length > 1 ? args[1] : null;
        String store = args.length > 2 ? args[2] : null;
        Integer minDiscount = args.length > 3 ? Integer.parseInt(args[3]) : 0;
        
        // Check if already subscribed
        Optional<TelegramSubscriber> existingSubscriber = subscriberRepository.findByChatId(Long.parseLong(chatId));
        
        if (existingSubscriber.isPresent()) {
            TelegramSubscriber subscriber = existingSubscriber.get();
            subscriber.setCity(city);
            subscriber.setStore(store);
            subscriber.setMinDiscount(minDiscount);
            subscriber.setActive(true);
            subscriberRepository.save(subscriber);
            
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Ваши настройки подписки обновлены!")
                    .build());
        } else {
            // Create new subscriber
            TelegramSubscriber subscriber = TelegramSubscriber.builder()
                    .chatId(Long.parseLong(chatId))
                    .username(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .city(city)
                    .store(store)
                    .minDiscount(minDiscount)
                    .active(true)
                    .build();
            
            subscriberRepository.save(subscriber);
            
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы успешно подписались на уведомления о скидках!")
                    .build());
        }
    }

    @Transactional
    private void handleUnsubscribe(Message message) throws TelegramApiException {
        String chatId = message.getChatId().toString();
        
        Optional<TelegramSubscriber> existingSubscriber = subscriberRepository.findByChatId(Long.parseLong(chatId));
        
        if (existingSubscriber.isPresent()) {
            TelegramSubscriber subscriber = existingSubscriber.get();
            subscriber.setActive(false);
            subscriberRepository.save(subscriber);
            
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы отписались от уведомлений о скидках.")
                    .build());
        } else {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы не были подписаны на уведомления.")
                    .build());
        }
    }

    private void handleDiscountsCommand(Message message) throws TelegramApiException {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        
        // Parse command arguments
        String[] args = text.split("\\s+");
        String city = args.length > 1 ? args[1] : null;
        String store = args.length > 2 ? args[2] : null;
        Integer minDiscount = args.length > 3 ? Integer.parseInt(args[3]) : null;
        
        List<DiscountDto> discounts = discountService.getDiscounts(city, store, minDiscount);
        
        if (discounts.isEmpty()) {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text("К сожалению, скидок по вашим параметрам не найдено.")
                    .build());
        } else {
            StringBuilder response = new StringBuilder("Найденные скидки:\n\n");
            
            int count = 0;
            for (DiscountDto discount : discounts) {
                if (count >= 10) {
                    response.append("\n... и еще ").append(discounts.size() - 10).append(" скидок");
                    break;
                }
                
                response.append("🔥 ").append(discount.getProductName()).append("\n")
                        .append("💰 ").append(discount.getDiscountPrice()).append(" грн (было: ")
                        .append(discount.getOriginalPrice()).append(" грн)\n")
                        .append("📍 ").append(discount.getStore()).append(", ").append(discount.getCity()).append("\n")
                        .append("🏷️ Скидка: ").append(discount.getDiscountPercentage()).append("%\n")
                        .append("⏱️ До: ").append(discount.getExpiresAt().format(DATE_FORMATTER)).append("\n\n");
                
                count++;
            }
            
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(response.toString())
                    .build());
        }
    }

    private void handleGenericMessage(Message message) throws TelegramApiException {
        String chatId = message.getChatId().toString();
        
        // Create keyboard with main commands
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("/discounts"));
        row1.add(new KeyboardButton("/subscribe"));
        
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("/unsubscribe"));
        row2.add(new KeyboardButton("/help"));
        
        keyboard.add(row1);
        keyboard.add(row2);
        
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        
        execute(SendMessage.builder()
                .chatId(chatId)
                .text("Используйте команды для взаимодействия с ботом. Нажмите /help для просмотра доступных команд.")
                .replyMarkup(keyboardMarkup)
                .build());
    }

    /**
     * Send a notification to a specific subscriber about new discounts
     */
    public void sendDiscountNotification(TelegramSubscriber subscriber, List<DiscountDto> newDiscounts) {
        if (!subscriber.isActive() || newDiscounts.isEmpty()) {
            return;
        }
        
        StringBuilder message = new StringBuilder("🎉 Новые скидки специально для вас!\n\n");
        
        int count = 0;
        for (DiscountDto discount : newDiscounts) {
            if (count >= 5) {
                message.append("\n... и еще ").append(newDiscounts.size() - 5).append(" скидок. Отправьте /discounts для просмотра всех скидок.");
                break;
            }
            
            message.append("🔥 ").append(discount.getProductName()).append("\n")
                    .append("💰 ").append(discount.getDiscountPrice()).append(" грн (было: ")
                    .append(discount.getOriginalPrice()).append(" грн)\n")
                    .append("📍 ").append(discount.getStore()).append(", ").append(discount.getCity()).append("\n")
                    .append("🏷️ Скидка: ").append(discount.getDiscountPercentage()).append("%\n")
                    .append("⏱️ До: ").append(discount.getExpiresAt().format(DATE_FORMATTER)).append("\n\n");
            
            count++;
        }
        
        try {
            execute(SendMessage.builder()
                    .chatId(subscriber.getChatId())
                    .text(message.toString())
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error sending discount notification to {}: {}", subscriber.getChatId(), e.getMessage(), e);
        }
    }

    /**
     * Send notifications to all active subscribers about new discounts
     */
    @Transactional(readOnly = true)
    public void notifySubscribersAboutNewDiscounts(List<DiscountDto> newDiscounts) {
        if (newDiscounts.isEmpty()) {
            return;
        }
        
        List<TelegramSubscriber> activeSubscribers = subscriberRepository.findByActiveTrue();
        
        for (TelegramSubscriber subscriber : activeSubscribers) {
            // Filter discounts based on subscriber preferences
            List<DiscountDto> filteredDiscounts = newDiscounts.stream()
                    .filter(d -> {
                        boolean matchesCity = subscriber.getCity() == null || subscriber.getCity().equals(d.getCity());
                        boolean matchesStore = subscriber.getStore() == null || subscriber.getStore().equals(d.getStore());
                        boolean matchesDiscount = d.getDiscountPercentage() >= subscriber.getMinDiscount();
                        return matchesCity && matchesStore && matchesDiscount;
                    })
                    .toList();
            
            if (!filteredDiscounts.isEmpty()) {
                sendDiscountNotification(subscriber, filteredDiscounts);
            }
        }
    }
}
