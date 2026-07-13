import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

fun main(): Unit = runBlocking(Dispatchers.Default) {
    val counter = AtomicInteger(0)

    // Rendezvous канал (правильное использование)
    val channel = Channel<Int>()  // capacity = 0 по умолчанию

    // Запускаем потребителя ПЕРВЫМ
    val consumer = launch {
        var received = 0
        for (value in channel) {
            println("📥 Получено: $value")
            received++
            delay(100)  // Медленная обработка
            // Ограничиваем количество
        }
    }

    delay(100)  // Даем потребителю запуститься

    // Запускаем производителей
    repeat(20) {
        launch {
            val value = counter.getAndIncrement()
            println("📤 Пытаюсь отправить: $value")
            channel.send(value)  // Будет ждать готовности потребителя
            println("✅ $value отправлен")
            delay(50)
        }
    }

    delay(5000)
    channel.close()
    consumer.cancel()

    println("\n📊 Итог:")
    println("   Всего создано значений: ${counter.get()}")
    println("   capacity канала: 0 (rendezvous)")
}