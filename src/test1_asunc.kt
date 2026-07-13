import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.time.Duration.Companion.milliseconds

fun main():Unit = runBlocking { // this: CoroutineScope

    val job = async {
        delay(1000)
        println("hello world")
    }

    println(job.await())

    Dispatchers
}