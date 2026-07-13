import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class Order(
    val id: Int,
    val items: List<String>,
    val priority: Priority = Priority.NORMAL
)

enum class Priority { HIGH, NORMAL, LOW }

class OrderProcessingSystem {
    // Каналы для разных этапов обработки
    private val newOrdersChannel = Channel<Order>(Channel.UNLIMITED)
    private val paymentChannel = Channel<Order>(capacity = 10)
    private val kitchenChannel = Channel<Order>(capacity = 5)
    private val deliveryChannel = Channel<Order>(capacity = 20)

    // Статистика
    private val stats = mutableMapOf<String, Int>()

    suspend fun startProcessing() = coroutineScope {
        println("🚀 Запуск системы обработки заказов...")

        // 1. Прием новых заказов (Producer)
        launch { receiveNewOrders() }

        // 2. Валидация заказов
        launch { validateOrders() }

        // 3. Обработка платежей
        repeat(3) { i -> // 3 кассира
            launch { processPayments("Кассир-$i") }
        }

        // 4. Кухня (ограниченная емкость)
        repeat(2) { i -> // 2 повара
            launch { cookOrders("Повар-$i") }
        }

        // 5. Доставка
        repeat(5) { i -> // 5 курьеров
            launch { deliverOrders("Курьер-$i") }
        }

        // 6. Мониторинг
        launch { monitorSystem() }

        delay(30_000)  // Работаем 30 секунд
        stopSystem()
    }

    // 📥 Прием новых заказов
    private suspend fun receiveNewOrders() {
        var orderId = 1
        while (isActive) {
            val order = generateRandomOrder(orderId++)
            println("📥 Поступил заказ #${order.id} (${order.items.size} товаров)")

            newOrdersChannel.send(order)
            updateStats("received")

            delay((100..500).random().toLong())  // Новые заказы каждые 100-500мс
        }
    }

    // ✅ Валидация заказов
    private suspend fun validateOrders() {
        for (order in newOrdersChannel) {
            if (isValid(order)) {
                println("✅ Заказ #${order.id} прошел валидацию")
                paymentChannel.send(order)
                updateStats("validated")
            } else {
                println("❌ Заказ #${order.id} не прошел валидацию")
                updateStats("rejected")
            }
        }
    }

    // 💰 Обработка платежей
    private suspend fun processPayments(cashierName: String) {
        for (order in paymentChannel) {
            println("💰 [$cashierName] Обрабатываю платеж заказ #${order.id}")

            val success = processPayment(order)
            if (success) {
                println("✅ [$cashierName] Платеж заказа #${order.id} успешен")
                kitchenChannel.send(order)
                updateStats("paid")
            } else {
                println("❌ [$cashierName] Платеж заказа #${order.id} отклонен")
                updateStats("payment_failed")
            }

            delay((200..800).random().toLong())  // Имитация обработки платежа
        }
    }

    // 👨‍🍳 Приготовление заказов
    private suspend fun cookOrders(chefName: String) {
        for (order in kitchenChannel) {
            println("👨‍🍳 [$chefName] Готовлю заказ #${order.id}")

            order.items.forEach { item ->
                println("   🍳 Приготовление: $item")
                delay((300..1500).random().toLong())
            }

            println("✅ [$chefName] Заказ #${order.id} готов")
            deliveryChannel.send(order)
            updateStats("cooked")
        }
    }

    // 🚚 Доставка заказов
    private suspend fun deliverOrders(driverName: String) {
        for (order in deliveryChannel) {
            println("🚚 [$driverName] Доставляю заказ #${order.id}")

            delay((1000..3000).random().toLong())  // Имитация доставки

            println("🎉 [$driverName] Заказ #${order.id} доставлен!")
            updateStats("delivered")
        }
    }

    // 📊 Мониторинг системы
    private suspend fun monitorSystem() {
        while (isActive) {
            delay(5000)
            println("\n📊 === СТАТИСТИКА ЗА 5 СЕК === ")
            stats.forEach { (stage, count) ->
                println("   $stage: $count")
            }
            println("   Очередь платежей: ${paymentChannel.toList().size}")
            println("   Очередь кухни: ${kitchenChannel.toList().size}")
            println("   Очередь доставки: ${deliveryChannel.toList().size}")
            println("=============================\n")
        }
    }

    // 🛑 Остановка системы
    private fun stopSystem() {
        println("\n🛑 Остановка системы...")
        newOrdersChannel.close()
        paymentChannel.close()
        kitchenChannel.close()
        deliveryChannel.close()

        println("\n📈 ИТОГОВАЯ СТАТИСТИКА:")
        stats.forEach { (stage, count) ->
            println("   $stage: $count")
        }
    }

    // Вспомогательные методы
    private fun generateRandomOrder(id: Int): Order {
        val items = listOf("Пицца", "Бургер", "Салат", "Суши", "Кофе", "Чай")
        val selectedItems = List((1..3).random()) { items.random() }
        val priority = if ((1..10).random() == 1) Priority.HIGH else Priority.NORMAL

        return Order(id, selectedItems, priority)
    }

    private fun isValid(order: Order): Boolean = (1..20).random() != 1  // 95% валидны

    private suspend fun processPayment(order: Order): Boolean {
        return (1..10).random() != 1  // 90% успешных платежей
    }

    private fun updateStats(stage: String) {
        stats[stage] = stats.getOrDefault(stage, 0) + 1
    }
}


fun main() = runBlocking {
    val system = OrderProcessingSystem()
    system.startProcessing()
}