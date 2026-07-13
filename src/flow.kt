import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {

    flow {
        emit("fffd")
        throw RuntimeException("Ошибка!")
    }
        .catch { cause ->  // Ловит ошибки выше по цепочке
            println("Поймана ошибка: $cause")
            emit("fdsfdsfsd")  // Можем эмитить значение вместо ошибки
            // ИЛИ emitAll(anotherFlow)  // Переключиться на другой Flow
        }
        .retryWhen { cause, attempt ->
            println(attempt)
            true
        }
        .collect { value ->
            println("Получено: $value")  // 1, затем -1
        }



}