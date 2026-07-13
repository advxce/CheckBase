import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val newFlow = flow<Int> {
        repeat(20) {
            emit(it)
            delay(100)
        }
    }.throttleLatest(500)


    newFlow.collect {
        println(it)
    }

    delay(1000)
}

fun <T> Flow<T>.throttleLatest(timeMillis: Long): Flow<T> = channelFlow {
    var lastValue: T? = null
    var lastTime = 0L
    var job: Job? = null

    collect { value ->
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime >= timeMillis) {
            lastTime = currentTime
            lastValue = null
            send(value)
        } else {
            lastValue = value
            if (job?.isActive != true) {
                val waitTime = timeMillis - (currentTime - lastTime)
                job = launch {
                    delay(waitTime)
                    lastValue?.let {
                        lastTime = System.currentTimeMillis()
                        send(it)
                        lastValue = null
                    }
                }
            }
        }
    }
    job?.join()
    close()
}


