class Notifier {
    constructor() {
        this.interval = 60000;
        this.subscribers = JSON.parse(localStorage.getItem('subscribers') || '[]');
    }

    init() {
        this.startChecking();
        document.getElementById('notification-settings-btn').addEventListener('click', () => {
            const panel = document.createElement('div');
            panel.className = 'settings-panel';
            panel.innerHTML = `
                <h3>Настройки уведомлений</h3>
                <p>Уведомления включены для ${this.subscribers.length} подписчиков</p>
                <button class="settings-close">Закрыть</button>
            `;
            document.body.appendChild(panel);
            panel.querySelector('.settings-close').addEventListener('click', () => panel.remove());
        });
    }

    startChecking() {
        setInterval(() => {
            const discounts = JSON.parse(localStorage.getItem('discounts') || '[]');
            discounts.forEach(d => {
                if (new Date(d.endDate) < new Date() && !d.notified) {
                    this.notifyExpired(d);
                    d.notified = true;
                    localStorage.setItem('discounts', JSON.stringify(discounts));
                }
            });
        }, this.interval);
    }

    notifyExpired(discount) {
        this.subscribers.forEach(id => {
            fetch(`https://api.telegram.org/bot${App.botToken}/sendMessage`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    chat_id: id,
                    text: `Акция ${discount.brand} (${discount.city}) завершилась!`
                })
            }).catch(error => console.error(`Ошибка уведомления ${id}:`, error));
        });
    }
}

const notifier = new Notifier();
document.addEventListener('DOMContentLoaded', () => notifier.init());