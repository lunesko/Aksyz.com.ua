# README.md

# АкцизUA

Агрегатор скидок на алкоголь и табачную продукцию в Украине.

![CI](https://github.com/lunesko/Aksyz.com.ua/actions/workflows/build-and-deploy.yml/badge.svg)

## 🌐 Демо
- 🖥️ Веб-приложение: [акциз.укр](https://акциз.укр) *(если развернуто)*
- 📱 Telegram-бот: [@akcizua_bot](https://t.me/akcizua_bot)
- 📘 Swagger UI: [`/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

## 🔧 Технологии
- **Backend**: Java 17+, Spring Boot, PostgreSQL, Liquibase
- **Frontend**: HTML + CSS + Vanilla JS (PWA)
- **CI/CD**: Docker, GitHub Actions
- **Парсинг**: Jsoup
- **Уведомления**: Telegram API + Web Push

## 🚀 Быстрый старт

```bash
git clone https://github.com/lunesko/Aksyz.com.ua.git
cd Aksyz.com.ua
docker-compose up --build
```

## 📂 Структура проекта

```
Aksyz.com.ua/
├── backend/          # Spring Boot backend
├── frontend/         # PWA frontend (Vanilla JS)
├── docker-compose.yml
└── .github/workflows/ # GitHub Actions
```

Подробнее в [INSTALL.md](INSTALL.md)

---

# INSTALL.md

# Установка и запуск проекта "АкцизUA"

## ⚙️ Требования
- Docker + Docker Compose
- JDK 17 (для разработки)
- Node.js (опционально, если править фронтенд локально)

## 🔥 Быстрый запуск через Docker
```bash
git clone https://github.com/lunesko/Aksyz.com.ua.git
cd Aksyz.com.ua
docker-compose up --build
```

Доступы:
- Frontend: http://localhost
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- PgAdmin: http://localhost:5050 *(admin / admin)*

## ⚙️ Переменные окружения
Создайте `.env` файл или используйте переменные по умолчанию:
```env
TELEGRAM_BOT_TOKEN=your_bot_token
TELEGRAM_BOT_USERNAME=your_bot_username
WEB_PUSH_PUBLIC_KEY=your_public_key
WEB_PUSH_PRIVATE_KEY=your_private_key
```

## 💡 Полезные команды
```bash
docker-compose down         # Остановить все контейнеры
docker-compose logs -f      # Логи в реальном времени
```

---

# CONTRIBUTING.md

# 🧑‍💻 Как внести вклад в проект "АкцизUA"

Спасибо за интерес к проекту! Ниже несколько рекомендаций для успешного участия.

## 🛠 Требования для разработки
- JDK 17+, Maven
- Node.js + npm (если работаете с frontend)
- PostgreSQL или Docker

## 📁 Как начать
```bash
git clone https://github.com/lunesko/Aksyz.com.ua.git
cd Aksyz.com.ua
```

## 🚀 Локальный запуск
Используйте Docker Compose:
```bash
docker-compose up --build
```

## 🌟 Рекомендации
- Следуйте стилю кода Java (Lombok, REST API), JS (ES6)
- Пишите понятные коммиты
- Описывайте pull request'ы: что сделано, зачем и как протестировать
- Добавляйте javadoc/комментарии к важным методам и DTO

## 📄 Лицензия
MIT License — свободно используйте и модифицируйте проект

---

С вопросами и предложениями — [открывайте issue](https://github.com/lunesko/Aksyz.com.ua/issues) 🙌

