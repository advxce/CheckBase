package asyncTasks

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withTimeout

fun main(): Unit = runBlocking {

    raceWithTimeout(block1 = {
        delay(5000)
        println("Hello")
    }, block2 = {
        delay(6000)
        println("World")
    })

    delay(3000)
}

//Реализуйте функцию, которая выполняет две асинхронные задачи
// и возвращает результат той, которая выполнится быстрее, но с таймаутом в 2 секунды.
suspend fun <T> raceWithTimeout(
    block1: suspend () -> T,
    block2: suspend () -> T,
    timeout: Long = 2000
): T = coroutineScope {
    val deferred1 = async{ block1() }
    val deferred2 = async{ block2() }

    try {
        withTimeout(timeout){
            select {
                deferred1.onAwait{ result->
                    deferred2.cancel()
                    result
                }
                deferred2.onAwait{ result->
                    deferred1.cancel()
                    result
                }
            }
        }
    } catch (e: TimeoutCancellationException){
        deferred1.cancel()
        deferred2.cancel()
        throw e
    }

}