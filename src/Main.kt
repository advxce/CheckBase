import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

sealed class CounterMsg {
    object Increment : CounterMsg()
    class GetCounter(val counter: CompletableDeferred<Int>) : CounterMsg()
}


@OptIn(ObsoleteCoroutinesApi::class)
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0

    for (msg in channel) {
        when (msg) {
            is CounterMsg.Increment -> counter++
            is CounterMsg.GetCounter -> msg.counter.complete(counter)
        }
    }
}

fun main(): kotlin.Unit = runBlocking {
    val counter = counterActor()
    val n = 1000
    val jobs = List(n) {
        launch {
            repeat(10) {
                counter.send(CounterMsg.Increment)
            }
        }
    }

    jobs.joinAll()
    val response = CompletableDeferred<Int>()
    counter.send(CounterMsg.GetCounter(response))
    println("Counter = ${response.await()}")
    counter.close()
}