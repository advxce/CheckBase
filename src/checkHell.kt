import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

fun main() = runBlocking {
    println("Main started in ${Thread.currentThread().name}")

    val job = launch(start = CoroutineStart.UNDISPATCHED) {
        // Начинает выполнение В ТЕКУЩЕМ потоке
        println("Started in ${Thread.currentThread().name}")

        // После первой suspend функции переключается на диспетчер
        delay(10)
        println("After delay in ${Thread.currentThread().name}")
    }

    job.join()
    println("Main finished")
}

suspend fun withTimeoutOrCancel() = coroutineScope {
    val mutex = Mutex()

    var counter = 0

    repeat(10000){
        launch(context = Dispatchers.IO, start =  CoroutineStart.DEFAULT) {
            mutex.withLock {
                counter++
            }
        }
    }

    delay(100)

    println("Counter: $counter")

}