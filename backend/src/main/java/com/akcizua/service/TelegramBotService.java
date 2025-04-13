package com.akcizua.service;

import com.akcizua.dto.DiscountDto;
import com.akcizua.model.TelegramSubscriber;
import com.akcizua.repository.TelegramSubscriberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
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
@RequiredArgsConstructor
@Slf4j
public class TelegramBotService extends TelegramWebhookBot {

    private final TelegramSubscriberRepository subscriberRepository;
    private final DiscountService discountService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.webhook-path}")
    private String webhookPath;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @PostConstruct
    public void init() {
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url("https://aksyz.com.ua/telegram")
                    .build();

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this, setWebhook);

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
    public String getBotPath() {
        return webhookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            String chatId = message.getChatId().toString();

            try {
                if (text.startsWith("/start")) {
                    return sendMessage(chatId, getWelcomeText());
                } else if (text.startsWith("/subscribe")) {
                    handleSubscribe(message);
                    return sendMessage(chatId, "Вы успешно подписались на уведомления о скидках!");
                } else if (text.startsWith("/unsubscribe")) {
                    handleUnsubscribe(message);
                    return sendMessage(chatId, "Вы отписались от уведомлений.");
                } else if (text.startsWith("/discounts")) {
                    return handleDiscountsCommand(message);
                } else if (text.startsWith("/help")) {
                    return sendMessage(chatId, getHelpText());
                } else {
                    return handleGenericMessage(chatId);
                }
            } catch (Exception e) {
                log.error("Error processing message from {}: {}", chatId, e.getMessage(), e);
                return sendMessage(chatId, "Произошла ошибка при обработке вашего запроса. Попробуйте позже.");
            }
        }
        return null;
    }

    private String getWelcomeText() {
        return "Добро пожаловать в АкцизUA - ваш персональный агрегатор скидок!\n" +
                "\nКоманды:\n" +
                "/subscribe - подписаться на уведомления\n" +
                "/unsubscribe - отписаться от уведомлений\n" +
                "/discounts - показать скидки\n" +
                "/help - справка";
    }

    private String getHelpText() {
        return "Команды:\n" +
                "/subscribe [город] [магазин] [мин_скидка]\n" +
                "/unsubscribe\n" +
                "/discounts [город] [магазин] [мин_скидка]\n" +
                "/help";
    }

    private BotApiMethod<?> sendMessage(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    private BotApiMethod<?> handleDiscountsCommand(Message message) {
        String chatId = message.getChatId().toString();
        String[] args = message.getText().split("\\s+");
        String city = args.length > 1 ? args[1] : null;
        String store = args.length > 2 ? args[2] : null;
        Integer minDiscount = args.length > 3 ? Integer.parseInt(args[3]) : null;

        List<DiscountDto> discounts = discountService.getDiscounts(city, store, minDiscount);

        if (discounts.isEmpty()) {
            return sendMessage(chatId, "Скидок не найдено по заданным параметрам.");
        }

        StringBuilder response = new StringBuilder("Найденные скидки:\n\n");
        int count = 0;
        for (DiscountDto d : discounts) {
            if (count++ >= 10) break;
            response.append("🔥 ").append(d.getProductName()).append("\n")
                    .append("💰 ").append(d.getDiscountPrice()).append(" грн (было: ")
                    .append(d.getOriginalPrice()).append(")\n")
                    .append("📍 ").append(d.getStore()).append(", ").append(d.getCity()).append("\n")
                    .append("🏷️ Скидка: ").append(d.getDiscountPercentage()).append("%\n")
                    .append("⏱️ До: ").append(d.getExpiresAt().format(DATE_FORMATTER)).append("\n\n");
        }

        return sendMessage(chatId, response.toString());
    }

    @Transactional
    public void handleSubscribe(Message message) {
        String chatId = message.getChatId().toString();
        User user = message.getFrom();
        String[] args = message.getText().split("\\s+");

        String city = args.length > 1 ? args[1] : null;
        String store = args.length > 2 ? args[2] : null;
        Integer minDiscount = args.length > 3 ? Integer.parseInt(args[3]) : 0;

        TelegramSubscriber subscriber = subscriberRepository.findByChatId(Long.parseLong(chatId))
                .orElse(TelegramSubscriber.builder()
                        .chatId(Long.parseLong(chatId))
                        .username(user.getUserName())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build());

        subscriber.setCity(city);
        subscriber.setStore(store);
        subscriber.setMinDiscount(minDiscount);
        subscriber.setActive(true);
        subscriberRepository.save(subscriber);
    }

    @Transactional
    public void handleUnsubscribe(Message message) {
        String chatId = message.getChatId().toString();
        Optional<TelegramSubscriber> optional = subscriberRepository.findByChatId(Long.parseLong(chatId));
        optional.ifPresent(subscriber -> {
            subscriber.setActive(false);
            subscriberRepository.save(subscriber);
        });
    }

    private BotApiMethod<?> handleGenericMessage(String chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("/subscribe"));
        row1.add(new KeyboardButton("/unsubscribe"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("/discounts"));
        row2.add(new KeyboardButton("/help"));

        rows.add(row1);
        rows.add(row2);

        keyboard.setKeyboard(rows);
        keyboard.setResizeKeyboard(true);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Выберите команду")
                .replyMarkup(keyboard)
                .build();
    }
}