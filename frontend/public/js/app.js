const App = {
    map: null,
    subscribers: JSON.parse(localStorage.getItem('subscribers') || '[]'),
    botToken: '7143397833:AAGESTtAcwBiU93ZV2klZz83AnQN-k5a3yk', // В продакшене токен должен быть на сервере
    editId: null,
    stores: {
        atb: {
            name: "АТБ",
            promotions: [
                { city: "kyiv", title: "Скидка 20% на пиво", description: "Пиво Балтика 0.5л - скидка 20% до 15.04.2025", image: "https://via.placeholder.com/300" },
                { city: "lviv", title: "Водка Хлебный Дар - 15% скидка", description: "Водка Хлебный Дар 0.7л - скидка 15% до 10.04.2025", image: "https://via.placeholder.com/300" },
                { city: "odesa", title: "Сигареты Marlboro - 10% скидка", description: "Сигареты Marlboro - скидка 10% до 20.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kharkiv", title: "Вино Лыхны - 25% скидка", description: "Вино Лыхны 0.75л - скидка 25% до 18.04.2025", image: "https://via.placeholder.com/300" },
                { city: "dnipro", title: "Виски Jim Beam - 20% скидка", description: "Виски Jim Beam 0.7л - скидка 20% до 22.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        dastor: {
            name: "Дастор",
            promotions: [
                { city: "kharkiv", title: "Вино Коблево - 25% скидка", description: "Вино Коблево 0.75л - скидка 25% до 12.04.2025", image: "https://via.placeholder.com/300" },
                { city: "dnipro", title: "Виски Jack Daniel's - 20% скидка", description: "Виски Jack Daniel's 0.7л - скидка 20% до 18.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kyiv", title: "Пиво Оболонь - 15% скидка", description: "Пиво Оболонь 0.5л - скидка 15% до 20.04.2025", image: "https://via.placeholder.com/300" },
                { city: "lviv", title: "Сигареты Winston - 10% скидка", description: "Сигареты Winston - скидка 10% до 15.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        klass: {
            name: "Класс",
            promotions: [
                { city: "kyiv", title: "Пиво Лидское - 15% скидка", description: "Пиво Лидское 0.5л - скидка 15% до 22.04.2025", image: "https://via.placeholder.com/300" },
                { city: "zaporizhzhia", title: "Водка Nemiroff - 10% скидка", description: "Водка Nemiroff 0.5л - скидка 10% до 25.04.2025", image: "https://via.placeholder.com/300" },
                { city: "odesa", title: "Вино Шабо - 20% скидка", description: "Вино Шабо 0.75л - скидка 20% до 18.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kharkiv", title: "Сигареты Camel - 15% скидка", description: "Сигареты Camel - скидка 15% до 20.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        alma: {
            name: "Alma",
            promotions: [
                { city: "lviv", title: "Сигареты Winston - 20% скидка", description: "Сигареты Winston - скидка 20% до 15.04.2025", image: "https://via.placeholder.com/300" },
                { city: "odesa", title: "Вино Шабо - 30% скидка", description: "Вино Шабо 0.75л - скидка 30% до 10.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kyiv", title: "Водка Finlandia - 15% скидка", description: "Водка Finlandia 0.7л - скидка 15% до 22.04.2025", image: "https://via.placeholder.com/300" },
                { city: "dnipro", title: "Пиво Черниговское - 10% скидка", description: "Пиво Черниговское 0.5л - скидка 10% до 18.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        rost: {
            name: "Рост",
            promotions: [
                { city: "kyiv", title: "Виски Johnnie Walker - 15% скидка", description: "Виски Johnnie Walker 0.7л - скидка 15% до 20.04.2025", image: "https://via.placeholder.com/300" },
                { city: "dnipro", title: "Пиво Оболонь - 10% скидка", description: "Пиво Оболонь 0.5л - скидка 10% до 18.04.2025", image: "https://via.placeholder.com/300" },
                { city: "lviv", title: "Сигареты Parliament - 20% скидка", description: "Сигареты Parliament - скидка 20% до 15.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kharkiv", title: "Вино Котнар - 25% скидка", description: "Вино Котнар 0.75л - скидка 25% до 22.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        rozetka: {
            name: "Rozetka",
            promotions: [
                { city: "kyiv", title: "Виски Chivas Regal - 15% скидка", description: "Виски Chivas Regal 0.7л - скидка 15% до 20.04.2025", image: "https://via.placeholder.com/300" },
                { city: "lviv", title: "Пиво Heineken - 20% скидка", description: "Пиво Heineken 0.5л - скидка 20% до 15.04.2025", image: "https://via.placeholder.com/300" },
                { city: "odesa", title: "Вино Inkerman - 10% скидка", description: "Вино Inkerman 0.75л - скидка 10% до 18.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        abs: {
            name: "ABS",
            promotions: [
                { city: "odesa", title: "Сигареты L&M - 25% скидка", description: "Сигареты L&M - скидка 25% до 18.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kharkiv", title: "Водка Absolut - 20% скидка", description: "Водка Absolut 0.7л - скидка 20% до 22.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kyiv", title: "Пиво Stella Artois - 15% скидка", description: "Пиво Stella Artois 0.5л - скидка 15% до 20.04.2025", image: "https://via.placeholder.com/300" }
            ]
        },
        fora: {
            name: "Fora",
            promotions: [
                { city: "dnipro", title: "Вино Marengo - 20% скидка", description: "Вино Marengo 0.75л - скидка 20% до 15.04.2025", image: "https://via.placeholder.com/300" },
                { city: "zaporizhzhia", title: "Сигареты Kent - 15% скидка", description: "Сигареты Kent - скидка 15% до 20.04.2025", image: "https://via.placeholder.com/300" },
                { city: "kyiv", title: "Пиво Bud - 10% скидка", description: "Пиво Bud 0.5л - скидка 10% до 18.04.2025", image: "https://via.placeholder.com/300" }
            ]
        }
    },
    popularProducts: [
        { name: "Пиво Балтика", store: "АТБ", price: "25 грн", image: "https://via.placeholder.com/300" },
        { name: "Водка Хлебный Дар", store: "Дастор", price: "120 грн", image: "https://via.placeholder.com/300" },
        { name: "Сигареты Marlboro", store: "Alma", price: "60 грн", image: "https://via.placeholder.com/300" },
        { name: "Вино Коблево", store: "Рост", price: "80 грн", image: "https://via.placeholder.com/300" },
        { name: "Виски Johnnie Walker", store: "Rozetka", price: "900 грн", image: "https://via.placeholder.com/300" },
        { name: "Пиво Черниговское", store: "Fora", price: "28 грн", image: "https://via.placeholder.com/300" },
        { name: "Сигареты Winston", store: "ABS", price: "58 грн", image: "https://via.placeholder.com/300" }
    ],
    translations: {
        ru: {
            nav_discounts: 'Скидки', nav_stores: 'Магазины', nav_map: 'Карта', nav_add: 'Добавить', nav_gallery: 'Галерея',
            select_city: 'Выберите город', hero_title: 'Лучшие скидки на алкоголь и сигареты',
            hero_subtitle: 'Найдите выгодные предложения в вашем городе и делитесь находками!',
            search_placeholder: 'Поиск (например, "пиво", "виски", "Marlboro")', discounts_title: 'Последние скидки',
            gallery_title: 'Галерея скидок', map_title: 'Скидки на карте',
            add_title: 'Добавить скидку', product_type: 'Тип продукта', select_type: 'Выберите тип продукта',
            whiskey: 'Виски', beer: 'Пиво', wine: 'Вино', vodka: 'Водка', cigarettes: 'Сигареты',
            brand: 'Бренд', brand_placeholder: 'Например: Jack Daniel\'s', description: 'Описание',
            description_placeholder: 'Детали акции, цены, условия', photo: 'Фото', upload: 'Загрузить',
            add_button: 'Добавить', success_message: 'Скидка успешно добавлена!', fill_fields: 'Заполните все поля'
        },
        ua: {
            nav_discounts: 'Знижки', nav_stores: 'Магазини', nav_map: 'Карта', nav_add: 'Додати', nav_gallery: 'Галерея',
            select_city: 'Оберіть місто', hero_title: 'Найкращі знижки на алкоголь та сигарети',
            hero_subtitle: 'Знайдіть вигідні пропозиції у вашому місті та діліться знахідками!',
            search_placeholder: 'Пошук (наприклад, "пиво", "віскі", "Marlboro")', discounts_title: 'Останні знижки',
            gallery_title: 'Галерея знижок', map_title: 'Знижки на карті',
            add_title: 'Додати знижку', product_type: 'Тип продукту', select_type: 'Оберіть тип продукту',
            whiskey: 'Віскі', beer: 'Пиво', wine: 'Вино', vodka: 'Горілка', cigarettes: 'Сигарети',
            brand: 'Бренд', brand_placeholder: 'Наприклад: Jack Daniel\'s', description: 'Опис',
            description_placeholder: 'Деталі акції, ціни, умови', photo: 'Фото', upload: 'Завантажити',
            add_button: 'Додати', success_message: 'Знижка успішно додана!', fill_fields: 'Заповніть усі поля'
        }
    },

    init() {
        this.loadDiscounts();
        this.renderStores();
        this.renderPopularProducts();
        this.setupListeners();
        this.initMap();
        this.initLanguage();
        this.startTimers();
    },

    sanitizeInput(input) {
        const div = document.createElement('div');
        div.textContent = input;
        return div.innerHTML;
    },

    loadDiscounts() {
        const discounts = JSON.parse(localStorage.getItem('discounts') || '[]');
        this.renderDiscounts(discounts, '#discount-grid');
        this.renderGallery(discounts);
    },

    setupListeners() {
        document.getElementById('mobileToggle').addEventListener('click', this.toggleMenu.bind(this));
        document.querySelectorAll('.nav-links a').forEach(link => {
            link.addEventListener('click', this.scrollTo.bind(this));
        });
        document.getElementById('add-form').addEventListener('submit', this.addDiscount.bind(this));
        document.getElementById('edit-form').addEventListener('submit', this.editDiscount.bind(this));
        document.querySelector('.search-icon').addEventListener('click', this.search.bind(this));
        document.querySelector('.search-input').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.search();
        });
        document.getElementById('store-filter').addEventListener('change', () => this.renderStores());
        document.getElementById('store-city-filter').addEventListener('change', () => this.renderStores());
        document.getElementById('product-type-filter').addEventListener('change', () => this.renderStores());
        document.getElementById('subscribe-btn').addEventListener('click', this.subscribe.bind(this));
        document.querySelectorAll('.modal-close').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('.modal').forEach(modal => modal.classList.remove('active'));
            });
        });
    },

    toggleMenu() {
        const nav = document.getElementById('navLinks');
        nav.classList.toggle('active');
        document.getElementById('mobileToggle').innerHTML = nav.classList.contains('active') ? '<i class="fas fa-times"></i>' : '<i class="fas fa-bars"></i>';
    },

    scrollTo(e) {
        e.preventDefault();
        const target = document.querySelector(e.target.getAttribute('href'));
        target.scrollIntoView({ behavior: 'smooth' });
        if (window.innerWidth <= 768) this.toggleMenu();
    },

    async addDiscount(e) {
        e.preventDefault();
        const form = e.target;
        const data = this.getFormData('#add-form');
        if (!data.type || !data.brand || !data.city || !data.description || !data.endDate || !data.price || !data.discountAmount) {
            alert(this.getTranslation('fill_fields'));
            return;
        }
        const discount = {
            id: Date.now(),
            type: this.sanitizeInput(data.type),
            brand: this.sanitizeInput(data.brand),
            city: this.sanitizeInput(data.city),
            description: this.sanitizeInput(data.description),
            endDate: this.sanitizeInput(data.endDate),
            price: parseFloat(data.price),
            discountAmount: parseFloat(data.discountAmount),
            date: new Date().toISOString(),
            views: 0,
            likes: 0,
            photos: Array.from(data.photos).map(file => URL.createObjectURL(file)),
            notified: false
        };
        try {
            await this.sendToTelegram(discount);
            this.notifySubscribers(discount);
            this.save('discounts', discount);
            this.renderDiscounts([discount], '#discount-grid');
            this.renderGallery([discount]);
            this.addMapMarker(discount);
            alert(this.getTranslation('success_message'));
            form.reset();
        } catch (error) {
            console.error('Ошибка при добавлении скидки:', error);
            alert('Произошла ошибка при добавлении скидки');
        }
    },

    editDiscount(e) {
        e.preventDefault();
        const form = e.target;
        const data = this.getFormData('#edit-form');
        let discounts = JSON.parse(localStorage.getItem('discounts') || '[]');
        const index = discounts.findIndex(d => d.id === this.editId);
        if (index !== -1) {
            discounts[index] = {
                ...discounts[index],
                type: this.sanitizeInput(data.type),
                brand: this.sanitizeInput(data.brand),
                city: this.sanitizeInput(data.city),
                description: this.sanitizeInput(data.description),
                endDate: this.sanitizeInput(data.endDate),
                price: parseFloat(data.price),
                discountAmount: parseFloat(data.discountAmount)
            };
            localStorage.setItem('discounts', JSON.stringify(discounts));
            this.loadDiscounts();
            this.updateMap();
            document.getElementById('edit-modal').classList.remove('active');
            alert('Скидка обновлена!');
        }
    },

    getFormData(selector) {
        const form = document.querySelector(selector);
        return {
            type: form.querySelector('[name="type"]').value,
            brand: form.querySelector('[name="brand"]').value,
            city: form.querySelector('[name="city"]').value,
            description: form.querySelector('[name="description"]').value,
            endDate: form.querySelector('[name="endDate"]').value,
            price: form.querySelector('[name="price"]').value,
            discountAmount: form.querySelector('[name="discountAmount"]').value,
            photos: form.querySelector('[name="photos"]')?.files || []
        };
    },

    save(key, item) {
        const items = JSON.parse(localStorage.getItem(key) || '[]');
        items.unshift(item);
        localStorage.setItem(key, JSON.stringify(items));
    },

    renderDiscounts(discounts, selector) {
        const grid = document.querySelector(selector);
        if (!grid) {
            console.error(`Контейнер для скидок (${selector}) не найден!`);
            return;
        }
        grid.innerHTML = '';
        discounts.forEach(d => {
            const card = document.createElement('div');
            card.className = 'card discount-card';
            card.dataset.id = d.id;
            card.innerHTML = `
                <div class="discount-img-container">
                    <img src="${d.photos[0] || 'https://via.placeholder.com/300'}" alt="${d.brand}" class="discount-img">
                    <div class="discount-badge">-${d.discountAmount}%</div>
                </div>
                <div class="discount-content">
                    <h3 class="discount-title">${d.brand}</h3>
                    <div class="discount-location"><i class="fas fa-map-marker-alt"></i> ${d.city}</div>
                    <p class="discount-description">${d.description}</p>
                    <p class="discount-price">Цена: ${d.price} грн</p>
                    <p class="discount-amount">Скидка: ${d.discountAmount}%</p>
                    <p class="discount-timer" data-end="${d.endDate}"></p>
                    <div class="discount-info">
                        <div><i class="far fa-calendar-alt"></i> ${new Date(d.date).toLocaleDateString()}</div>
                        <div><i class="far fa-eye"></i> ${d.views || 0}</div>
                        <div><i class="far fa-thumbs-up"></i> ${d.likes || 0}</div>
                    </div>
                    <div class="discount-actions">
                        <button class="btn edit-btn"><i class="fas fa-edit"></i></button>
                        <button class="btn delete-btn"><i class="fas fa-trash"></i></button>
                        <button class="btn like-btn"><i class="fas fa-thumbs-up"></i> ${d.likes || 0}</button>
                        <button class="btn share-btn"><i class="fas fa-share-alt"></i></button>
                    </div>
                </div>
            `;
            grid.appendChild(card);

            card.querySelector('.edit-btn').addEventListener('click', (e) => {
                e.stopPropagation();
                this.editId = d.id;
                document.getElementById('edit-type').value = d.type;
                document.getElementById('edit-brand').value = d.brand;
                document.getElementById('edit-city').value = d.city;
                document.getElementById('edit-description').value = d.description;
                document.getElementById('edit-end-date').value = d.endDate.split('T')[0];
                document.getElementById('edit-price').value = d.price;
                document.getElementById('edit-discount-amount').value = d.discountAmount;
                document.getElementById('edit-modal').classList.add('active');
            });

            card.querySelector('.delete-btn').addEventListener('click', (e) => {
                e.stopPropagation();
                if (confirm('Удалить скидку?')) {
                    discounts = discounts.filter(item => item.id !== d.id);
                    localStorage.setItem('discounts', JSON.stringify(discounts));
                    this.loadDiscounts();
                    this.updateMap();
                }
            });

            card.querySelector('.like-btn').addEventListener('click', (e) => {
                e.stopPropagation();
                d.likes = (d.likes || 0) + 1;
                localStorage.setItem('discounts', JSON.stringify(discounts));
                card.querySelector('.like-btn').innerHTML = `<i class="fas fa-thumbs-up"></i> ${d.likes}`;
            });

            card.querySelector('.share-btn').addEventListener('click', (e) => {
                e.stopPropagation();
                const text = `${d.brand} (${d.city}): ${d.description}`;
                navigator.clipboard.writeText(text).then(() => alert('Скопировано в буфер обмена!'));
            });
        });
    },

    renderStores() {
        const grid = document.getElementById('store-grid');
        if (!grid) {
            console.error('Контейнер для акций магазинов не найден!');
            return;
        }
        grid.innerHTML = '';
        const storeFilter = document.getElementById('store-filter').value;
        const cityFilter = document.getElementById('store-city-filter').value;
        const productFilter = document.getElementById('product-type-filter').value;
        let hasPromotions = false;

        Object.keys(this.stores).forEach(storeKey => {
            const store = this.stores[storeKey];
            store.promotions.forEach(promo => {
                const storeMatch = !storeFilter || storeKey === storeFilter;
                const cityMatch = !cityFilter || promo.city === cityFilter;
                const productMatch = !productFilter || promo.description.toLowerCase().includes(productFilter);
                if (storeMatch && cityMatch && productMatch) {
                    hasPromotions = true;
                    const card = document.createElement('div');
                    card.className = 'store-card';
                    card.dataset.store = storeKey;
                    card.dataset.city = promo.city;
                    card.innerHTML = `
                        <h3>${store.name} (${promo.city})</h3>
                        <div class="store-info">
                            <p><strong>${promo.title}</strong></p>
                            <p>${promo.description}</p>
                        </div>
                        <div class="store-details">
                            <img src="${promo.image}" alt="${promo.title}">
                        </div>
                    `;
                    grid.appendChild(card);
                    card.addEventListener('click', () => {
                        const isExpanded = card.classList.contains('expanded');
                        document.querySelectorAll('.store-card').forEach(c => c.classList.remove('expanded'));
                        if (!isExpanded) card.classList.add('expanded');
                    });
                }
            });
        });

        if (!hasPromotions) {
            grid.innerHTML = '<p style="text-align: center; color: #6c757d;">Акции для выбранного магазина и города не найдены.</p>';
        }
    },

    renderPopularProducts() {
        const grid = document.getElementById('popular-products-grid');
        if (!grid) return;
        grid.innerHTML = '';
        this.popularProducts.forEach(product => {
            const card = document.createElement('div');
            card.className = 'popular-product-card';
            card.innerHTML = `
                <img src="${product.image}" alt="${product.name}">
                <h3>${product.name}</h3>
                <p>Магазин: ${product.store}</p>
                <p>Цена: ${product.price}</p>
            `;
            grid.appendChild(card);
        });
    },

    renderGallery(discounts) {
        const grid = document.getElementById('gallery-grid');
        if (!grid) return;
        grid.innerHTML = '';
        discounts.forEach(d => {
            const item = document.createElement('div');
            item.className = 'gallery-item';
            item.dataset.id = d.id;
            item.innerHTML = `
                <img src="${d.photos[0] || 'https://via.placeholder.com/300'}" alt="${d.brand}" class="gallery-img">
                <div class="gallery-overlay">
                    <h3>${d.brand}</h3>
                    <div class="discount-location"><i class="fas fa-map-marker-alt"></i> ${d.city}</div>
                </div>
            `;
            grid.appendChild(item);
            item.addEventListener('click', () => {
                const modal = document.getElementById('gallery-modal-content');
                modal.innerHTML = `
                    <button type="button" class="modal-close" aria-label="Закрыть"><i class="fas fa-times"></i></button>
                    <img src="${d.photos[0] || 'https://via.placeholder.com/300'}" alt="${d.brand}">
                    <h3>${d.brand}</h3>
                    <p>${d.description}</p>
                    <p>Цена: ${d.price} грн</p>
                    <p>Скидка: ${d.discountAmount}%</p>
                    <div class="discount-location"><i class="fas fa-map-marker-alt"></i> ${d.city}</div>
                    <p class="discount-timer" data-end="${d.endDate}"></p>
                `;
                document.getElementById('gallery-modal').classList.add('active');
            });
        });
    },

    search() {
        const term = document.querySelector('.search-input').value.toLowerCase().trim();
        if (!term) return;
        const discounts = JSON.parse(localStorage.getItem('discounts') || '[]').filter(d =>
            d.brand.toLowerCase().includes(term) || d.description.toLowerCase().includes(term));
        this.renderDiscounts(discounts, '#discount-grid');
    },

    async sendToTelegram(discount) {
        const message = `Новая скидка!\nТип: ${discount.type}\nБренд: ${discount.brand}\nГород: ${discount.city}\nОписание: ${discount.description}\nЦена: ${discount.price} грн\nСкидка: ${discount.discountAmount}%\nДо: ${discount.endDate}`;
        try {
            const response = await fetch(`https://api.telegram.org/bot${this.botToken}/sendMessage`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ chat_id: '829741830', text: message })
            });
            if (!response.ok) throw new Error('Ошибка отправки в Telegram');
        } catch (error) {
            console.error('Ошибка отправки в Telegram:', error);
            throw error;
        }
    },

    notifySubscribers(discount) {
        this.subscribers.forEach(id => {
            fetch(`https://api.telegram.org/bot${this.botToken}/sendMessage`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    chat_id: id,
                    text: `Новая скидка: ${discount.brand} (${discount.city}) - ${discount.description}, Цена: ${discount.price} грн, Скидка: ${discount.discountAmount}%`
                })
            }).catch(error => console.error(`Ошибка уведомления подписчика ${id}:`, error));
        });
    },

    subscribe() {
        const id = document.getElementById('telegram-id').value.trim();
        if (!id) {
            alert('Введите Telegram ID');
            return;
        }
        if (!this.subscribers.includes(id)) {
            this.subscribers.push(id);
            try {
                localStorage.setItem('subscribers', JSON.stringify(this.subscribers));
                alert('Вы успешно подписаны!');
                document.getElementById('telegram-id').value = '';
            } catch (error) {
                console.error('Ошибка сохранения подписчиков:', error);
                alert('Произошла ошибка при подписке. Попробуйте снова.');
            }
        } else {
            alert('Вы уже подписаны!');
        }
    },

    initMap() {
        const mapContainer = document.getElementById('map-container');
        if (!mapContainer) {
            console.error('Контейнер для карты не найден!');
            return;
        }
        this.map = L.map('map-container', {
            maxBounds: [[44.3833, 22.1667], [52.45, 40.0]],
            maxBoundsViscosity: 1.0,
            minZoom: 6,
            maxZoom: 18
        }).setView([49.0, 31.5], 6);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(this.map);
        this.map.fitBounds([[44.3833, 22.1667], [52.45, 40.0]]);
        setTimeout(() => {
            this.updateMap();
            this.map.invalidateSize();
        }, 100);
    },

    updateMap() {
        if (!this.map) return;
        this.map.eachLayer(layer => layer instanceof L.Marker && this.map.removeLayer(layer));
        const discounts = JSON.parse(localStorage.getItem('discounts') || '[]');
        const cityCoords = {
            kyiv: [50.45, 30.52], kharkiv: [49.99, 36.23], odesa: [46.48, 30.73], dnipro: [48.45, 34.98], lviv: [49.84, 24.03],
            zaporizhzhia: [47.85, 35.12], vinnytsia: [49.23, 28.47], mykolaiv: [46.97, 31.99], kherson: [46.63, 32.61],
            cherkasy: [49.44, 32.06], chernivtsi: [48.29, 25.93], uzhhorod: [48.62, 22.29], zhytomyr: [50.25, 28.66],
            sumy: [50.91, 34.80], rivne: [50.62, 26.25], ternopil: [49.55, 25.59], lutsk: [50.74, 25.34],
            ivano_frankivsk: [48.92, 24.71], khmelnytskyi: [49.42, 26.98], poltava: [49.58, 34.54],
            kropyvnytskyi: [48.51, 32.26], chernihiv: [51.50, 31.28]
        };
        discounts.forEach(d => {
            if (cityCoords[d.city]) {
                L.marker(cityCoords[d.city]).addTo(this.map)
                    .bindPopup(`${d.brand}: ${d.description}, Цена: ${d.price} грн, Скидка: ${d.discountAmount}%`);
            }
        });
    },

    addMapMarker(discount) {
        if (!this.map) return;
        const cityCoords = {
            kyiv: [50.45, 30.52], kharkiv: [49.99, 36.23], odesa: [46.48, 30.73], dnipro: [48.45, 34.98], lviv: [49.84, 24.03],
            zaporizhzhia: [47.85, 35.12], vinnytsia: [49.23, 28.47], mykolaiv: [46.97, 31.99], kherson: [46.63, 32.61],
            cherkasy: [49.44, 32.06], chernivtsi: [48.29, 25.93], uzhhorod: [48.62, 22.29], zhytomyr: [50.25, 28.66],
            sumy: [50.91, 34.80], rivne: [50.62, 26.25], ternopil: [49.55, 25.59], lutsk: [50.74, 25.34],
            ivano_frankivsk: [48.92, 24.71], khmelnytskyi: [49.42, 26.98], poltava: [49.58, 34.54],
            kropyvnytskyi: [48.51, 32.26], chernihiv: [51.50, 31.28]
        };
        if (cityCoords[discount.city]) {
            const marker = L.marker(cityCoords[discount.city]).addTo(this.map)
                .bindPopup(`${discount.brand}: ${discount.description}, Цена: ${discount.price} грн, Скидка: ${discount.discountAmount}%`);
            marker.openPopup();
        }
    },

    startTimers() {
        setInterval(() => {
            document.querySelectorAll('.discount-timer').forEach(timer => {
                const end = new Date(timer.dataset.end);
                const now = new Date();
                const diff = end - now;
                if (diff > 0) {
                    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    timer.textContent = `Осталось: ${days} дн. ${hours} ч.`;
                } else {
                    timer.textContent = 'Акция закончилась';
                    timer.style.color = 'red';
                }
            });
        }, 1000);
    },

    initLanguage() {
        const lang = localStorage.getItem('language') || 'ru';
        document.querySelectorAll('[data-translate]').forEach(el => {
            const key = el.dataset.translate;
            if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
                el.placeholder = this.translations[lang][key] || el.placeholder;
            } else {
                el.textContent = this.translations[lang][key] || el.textContent;
            }
        });
    },

    getTranslation(key) {
        const lang = localStorage.getItem('language') || 'ru';
        return this.translations[lang][key];
    }
};

document.addEventListener('DOMContentLoaded', () => App.init());