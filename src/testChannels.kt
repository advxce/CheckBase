import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.security.auth.callback.Callback

fun main(): Unit = runBlocking {

//    var counter = 0
//
//    val channel = Channel<Int>()
//    repeat(100) {
//        launch {
//            try {
//                counter++
//                channel.send(counter)
//
//            } finally {
//                channel.close()
//            }
//
//        }
//
//    }
//
//
//    launch {
//        for (item in channel) {
//            println(item)
//        }
//
//    }

    flow {
        repeat(10) { i ->
            println("Emitting $i")
            emit(i)
            delay(100)
        }
    }
        .collectLatest { value ->  // ⚡ Отменяет предыдущую обработку
            println("Start processing $value")
            delay(300)  // Долгая обработка
            println("Finished processing $value")  // Не выполнится для многих значений
        }
}

