import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main(): Unit = runBlocking {

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Caught exception $exception")
    }

    val scope = CoroutineScope(SupervisorJob() + coroutineExceptionHandler)

    val job = scope.async {
        launch {
            while (true) {
                println("x")
                delay(100)
                println("after delay")
            }
        }
        println("something in async")
        throw Exception("throw exception")
    }

    delay(3000)
}