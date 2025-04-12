# Обзор проекта "АкцизUA"

Мы создали полноценный проект "АкцизUA" - агрегатор скидок на алкоголь и табачную продукцию в Украине, согласно указанным спецификациям. Вот основные компоненты, которые были реализованы:

## Бэкенд (Spring Boot + PostgreSQL + Liquibase)

1. **Основа Spring Boot приложения**:
   - Java 17+
   - Структура проекта с разделением на модели, репозитории, сервисы и контроллеры
   - Liquibase для управления миграциями БД

2. **API endpoints**:
   - `GET /api/discounts` - получение всех скидок с фильтрацией
   - `POST /api/discounts` - добавление новых скидок
   - `GET /api/discounts/filters` - получение доступных фильтров
   - Документация API с Swagger UI (`/swagger-ui/index.html`)

3. **Функциональность**:
   - CRUD операции для скидок
   - Фильтрация по городу, магазину, проценту скидки
   - Кэширование результатов
   - Парсинг скидок с сайтов с помощью Jsoup
   - Telegram бот с функциями подписки на обновления
   - Web Push уведомления

4. **Модели данных**:
   - Discount (скидки)
   - TelegramSubscriber (подписчики в Telegram)
   - PushSubscription (подписки на Web Push уведомления)

## Фронтенд (Vanilla JavaScript)

1. **Основная структура**:
   - HTML5 семантическая разметка
   - CSS с адаптивным дизайном
   - JavaScript без фреймворков

2. **Функциональность**:
   - Отображение скидок в виде карточек
   - Фильтрация по городу, магазину, проценту скидки
   - Подписка на Web Push уведомления
   - PWA (Progressive Web App) с поддержкой офлайн-режима
   - Service Worker для кэширования и push-уведомлений

3. **JS-модули**:
   - `api.js` - взаимодействие с REST API
   - `ui.js` - управление отображением данных
   - `main.js` - основная логика приложения

## Деплой и CI/CD

1. **Docker и docker-compose**:
   - Контейнеризация бэкенда и фронтенда
   - Конфигурация PostgreSQL
   - Настройка сети и томов

2. **GitHub Actions**:
   - Автоматическая сборка и тестирование
   - Сборка Docker-образов
   - Автоматический деплой на сервер

## Структура проекта

```
akcizua/
├── backend/                             # Spring Boot бэкенд
│   ├── src/main/java/com/akcizua/
│   │   ├── AkcizuaApplication.java      # Основной класс приложения
│   │   ├── controller/                  # REST контроллеры
│   │   │   ├── DiscountController.java
│   │   │   └── WebPushController.java
│   │   ├── dto/                         # Data Transfer Objects
│   │   │   ├── DiscountDto.java
│   │   │   └── PushSubscriptionDto.java
│   │   ├── model/                       # Модели данных
│   │   │   ├── Discount.java
│   │   │   ├── PushSubscription.java
│   │   │   └── TelegramSubscriber.java
│   │   ├── repository/                  # Репозитории для доступа к данным
│   │   │   ├── DiscountRepository.java
│   │   │   ├── PushSubscriptionRepository.java
│   │   │   └── TelegramSubscriberRepository.java
│   │   └── service/                     # Сервисы для бизнес-логики
│   │       ├── DiscountService.java
│   │       ├── WebPushService.java
│   │       ├── WebScraperService.java
│   │       ├── TelegramBotService.java
│   │       └── impl/
│   │           └── DiscountServiceImpl.java
│   ├── src/main/resources/
│   │   ├── application.properties       # Настройки приложения
│   │   └── db/changelog/                # Миграции Liquibase
│   │       ├── changelog-master.xml
│   │       └── changes/
│   │           ├── 001-create-discounts-table.xml
│   │           ├── 002-create-telegram-subscribers-table.xml
│   │           └── 003-create-push-subscriptions-table.xml
│   ├── pom.xml                          # Maven зависимости
│   └── Dockerfile                       # Docker конфигурация для бэкенда
├── frontend/                            # Vanilla JS фронтенд
│   ├── public/
│   │   ├── index.html                   # Основная HTML страница
│   │   ├── offline.html                 # Страница для офлайн-режима
│   │   ├── manifest.json                # Web App Manifest для PWA
│   │   ├── service-worker.js            # Service Worker для кэша и уведомлений
│   │   ├── css/
│   │   │   ├── normalize.css
│   │   │   └── styles.css               # Основные стили
│   │   ├── js/
│   │   │   ├── api.js                   # Взаимодействие с API
│   │   │   ├── ui.js                    # UI компоненты
│   │   │   └── main.js                  # Основная логика
│   │   └── images/
│   │       ├── logo.svg                 # Логотип приложения
│   │       └── icons/                   # Иконки для PWA
│   └── Dockerfile                       # Docker конфигурация для фронтенда
├── docker-compose.yml                   # Конфигурация для запуска всех сервисов
├── README.md                            # Документация проекта
└── .github/workflows/                   # GitHub Actions workflows
    └── build-and-deploy.yml             # CI/CD конфигурация
```

## Запуск проекта

1. **Клонировать репозиторий**:
   ```bash
   git clone https://github.com/yourusername/akcizua.git
   cd akcizua
   ```

2. **Запустить через Docker Compose**:
   ```bash
   docker-compose up --build
   ```

3. **Доступ**:
   - Фронтенд: http://localhost:80
   - Бэкенд API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/swagger-ui/index.html
   - PgAdmin: http://localhost:5050 (admin@akcizua.com / admin)

## Дальнейшие улучшения

1. **Безопасность**:
   - Добавить аутентификацию и авторизацию (JWT)
   - Ограничение CORS в production
   - HTTPS для всех сервисов

2. **Функциональность**:
   - Расширенная персонализация пользовательских предпочтений
   - Интеграция с другими мессенджерами (Viber, Facebook)
   - Аналитика популярности скидок

3. **Инфраструктура**:
   - Мониторинг (Prometheus + Grafana)
   - Логирование (ELK Stack)
   - Резервное копирование данных
