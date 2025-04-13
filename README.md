# АкцизUA

## Агрегатор скидок на алкоголь и табачную продукцию в Украине

АкцизUA - веб-приложение для отслеживания скидок на алкоголь и табачную продукцию в различных магазинах Украины. Приложение позволяет фильтровать скидки по городу, магазину и проценту скидки, а также получать уведомления о новых скидках через Telegram-бот и Web Push.

### Технологии

- **Backend**: Java 17+, Spring Boot, PostgreSQL, Liquibase
- **Frontend**: HTML + CSS + Vanilla JavaScript (Fetch API)
- **API Documentation**: OpenAPI 3 (Swagger UI)
- **Deployment**: Docker + docker-compose + CI/CD (GitHub Actions)

### Функциональность

1. Отображение скидок на товары с фильтрацией по городу, магазину, проценту скидки
2. Сервис парсинга скидок с помощью Jsoup, кэширование и запуск по расписанию
3. Telegram-бот с командами `/subscribe`, `/unsubscribe`, `/discounts`
4. Web Push уведомления (Service Worker, Push API)
5. Swagger UI с доступом по пути `/swagger-ui/index.html`

### Структура проекта

```
discount-aggregator/
├── backend/               # Spring Boot backend
│   └── ...                # config, controller, model, repository, service и т.д.
├── frontend/              # Vanilla JS frontend
│   └── public/            # index.html, js/, css/
├── docker-compose.yml
├── README.md
```

### API (RESTful)

- `GET /api/discounts` — получение всех скидок с фильтрами
- `POST /api/discounts` — добавление скидки (админ)

### Локальный запуск

```bash
# Клонирование репозитория
git clone https://github.com/yourusername/akcizua.git
cd akcizua

# Запуск проекта через Docker Compose
docker-compose up --build



```

После запуска:
- Frontend доступен по адресу: http://localhost:8080
- Swagger UI доступен по адресу: http://localhost:8080/swagger-ui/index.html

### Требования для разработки

- JDK 17+
- Docker и Docker Compose
- PostgreSQL (для локальной разработки без Docker)
- Node.js и npm (опционально, для локальной разработки фронтенда)

### CI/CD

Проект настроен на автоматическую сборку, тестирование и деплой с помощью GitHub Actions. При создании Pull Request или push в ветку main запускается workflow, который собирает Docker-образы и тестирует код.
