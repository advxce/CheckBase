import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.time.delay
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

val api = listOf<String>("ff", "lll", "kkk")

val list = mutableListOf(1, 2, 3, 4, 5, 6, 7)
fun main(): Unit = runBlocking {
  checkChannel()
}

//fun getData(scope: CoroutineScope) = MutableStateFlow<List<Int>>(list.toList())
//fun setData (elem:Int, stateFlow: MutableStateFlow<List<Int>>){
//    list.add(elem)
//    stateFlow.value = list
    //Actor - это корутина, которая получает сообщения через Channel и
// обрабатывает их последовательно. Это реализация паттерна Actor Model.
//}

suspend fun checkChannel() = coroutineScope {
    val channel = Channel<Int>(Channel.CONFLATED)  // Только последнее значение

    launch {
        repeat(5) { i ->
            channel.send(i)  // Старые значения теряются!
            println("📤 Отправил $i")
            delay(100)
        }
    }

    launch {
        delay(350)  // Пропускаем первые 3 значения
        val value = channel.receive()  // Получит только 3!
        println("📥 Получил: $value")  // 3 (1 и 2 потеряны)
    }
}

suspend fun generatorTicks() = coroutineScope{
    var counter = 0
    val replay = 100

    println("\n🎯 Пример 8: Биржевой тикер с настройками буфера")

    // Тикер акций: нужно последние 100 значений для графиков
    // + буфер на случай скачков
    val stockTicker = MutableSharedFlow<StockTick>(
        replay = replay,           // Для новых подписчиков - история
        extraBufferCapacity = 1000, // Буфер на скачки объема
        onBufferOverflow = BufferOverflow.DROP_OLDEST // В кризис теряем старые тики
    )

    // Генератор тиков (может быть очень быстрым)
    val tickerJob = launch {
        var price = 100.0
        var tickId = 0

        while (true) {
            // Имитация рыночных данных
            val change = (Math.random() - 0.5) * 2
            price += change

            stockTicker.emit(StockTick(tickId++, price, System.currentTimeMillis()))

            // Рынок может быть очень быстрым
            delay(100)
            counter++
        }
    }

    // Аналитик 1: Смотрит на последние 100 тиков
    launch {
        delay(5000) // Начинает смотреть через 5 секунд
        println("\n📈 Аналитик 1 подключился:")
        stockTicker.collect { tick ->
            // Получает всю историю + новые значения
            if (tick.id % 10 == 0) {
                println("   Аналитик 1: Тик #${tick.id}, цена: ${"%.2f".format(tick.price)}")
            }
        }
    }

    // Трейдер: Нужны только свежие данные
    launch {
        val traderFlow = stockTicker
            .drop(replay)  // Игнорируем историю
            .sample(100)   // Берем каждые 100мс

        traderFlow.collect { tick ->
            println(" $counter  💰 Трейдер: Новый тик #${tick.id}, цена: ${"%.2f".format(tick.price)}")
        }

    }

    delay(20000)
    tickerJob.cancel()
}

data class StockTick(val id: Int, val price: Double, val timestamp: Long)


suspend fun checkBuffered() = coroutineScope {
    println("\n🎯 Пример 4: Сравнение с и без extraBufferCapacity")

    // Без буфера - emit будет ждать
    val flowNoBuffer = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 0
    )

    // С буфером - emit не блокируется
    val flowWithBuffer = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 5
    )

    launch {
        println("\n🚀 Тест flowNoBuffer:")
        repeat(10) { i ->
            val start = System.currentTimeMillis()
            flowNoBuffer.emit(i)
            val time = System.currentTimeMillis() - start
            println("   Эмит $i занял ${time}ms")
            delay(50)
        }
    }

    delay(300) // Эмиттер успеет отправить несколько значений

    launch {
        flowNoBuffer.collect {
            println("   Получено: $it")
            delay(200) // Медленный коллектор
        }
    }

    delay(1500)



    // Тест с буфером
    println("\n🚀 Тест flowWithBuffer:")
    launch {
        repeat(10) { i ->
            val start = System.currentTimeMillis()
            flowWithBuffer.emit(i)
            val time = System.currentTimeMillis() - start
            println("   Эмит $i занял ${time}ms")
            delay(50)
        }
    }

    delay(300)

    launch {
        flowWithBuffer.collect {
            println("   Получено: $it")
            delay(200)
        }
    }

    delay(2000)
}



suspend fun checkFlows() = coroutineScope {
    val flowWithReplay = MutableSharedFlow<Int>(replay = 3)
    val flowNoReplay = MutableSharedFlow<Int>(replay = 0)


    launch {
        repeat(9){ i->
            delay(100)
            flowWithReplay.emit(i)
            flowNoReplay.emit(i)
        }
    }

    delay(680)

    launch {
        println("flow with replay")
        flowWithReplay.collect{
            println("get value with replay: $it")
        }
    }

delay(1000)
    launch {
        println("flowNoReplay")
        flowNoReplay.collect {
            println("get value: $it")
        }
    }

}


fun exampleColdFlow() = runBlocking {
    println("\n📦 ПРИМЕР 1: Cold Flow (каждый подписчик свой поток)")

    val coldFlow = flow {
        println("🤖 Cold Flow: Запуск генерации")
        repeat(5) { i ->
            delay(200.milliseconds)
            emit("Событие $i")
            println("🤖 Cold Flow: Эмитировано событие $i")
        }
        println("🤖 Cold Flow: Завершение генерации")
    }

    // Два независимых подписчика
    val subscriber1 = launch {
        coldFlow.collect { value ->
            println("👤 Подписчик 1: $value")
        }
    }

    delay(300.milliseconds) // Ждем немного

    val subscriber2 = launch {
        coldFlow.collect { value ->
            println("👤 Подписчик 2: $value")
        }
    }

    delay(1500.milliseconds)
    subscriber1.cancel()
    subscriber2.cancel()

    println("✅ Cold Flow: У каждого подписчика СВОЯ копия потока!")
}


fun exampleDifferentOperators() = runBlocking {
    println("\n🎛️ ПРИМЕР 4: Разные операторы для разных подписчиков")

    val numbersFlow = flow {
        repeat(15) { i ->
            delay(100.milliseconds)
            emit(i)
        }
    }.shareIn(this, SharingStarted.Lazily)

    // Подписчик 1: Только четные числа
    launch {
        numbersFlow
            .filter { it % 2 == 0 }
            .collect { value ->
                println("🔢 Четные: $value")
            }
    }

    delay(250.milliseconds)

    // Подписчик 2: Квадраты чисел
    launch {
        numbersFlow
            .map { it * it }
            .collect { value ->
                println("🧮 Квадрат: $value")
            }
    }

    delay(250.milliseconds)

    // Подписчик 3: Только первые 5 чисел
    launch {
        numbersFlow
            .take(5)
            .collect { value ->
                println("🎯 Первые 5: $value")
            }
    }

    delay(250.milliseconds)

    // Подписчик 4: Группировка по 3
    launch {
        numbersFlow
            .chunked(3)
            .collect { chunk ->
                println("📦 Группа: $chunk")
            }
    }

    delay(2000.milliseconds)
    println("✅ Каждый подписчик может трансформировать поток по-своему!")
}