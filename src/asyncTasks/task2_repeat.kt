package asyncTasks

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main():Unit = runBlocking {
    raceWithTimeoutRepeat(block1 = {
        delay(1000)
        println("Hello")
    }, block2 = {
        delay(500)
        println("World")
    })

    delay(3000)
}

suspend fun <T> raceWithTimeoutRepeat(
    block1: suspend () -> T,
    block2: suspend () -> T,
    timeout: Long = 2000
): T = coroutineScope {
    val deferred1 = async{  block1() }
    val deferred2 = async{ block2() }
    try {
        withTimeout(timeout){
           while (isActive){
               when{
                   deferred1.isCompleted ->{
                       deferred2.cancel()
                       return@withTimeout deferred1.await()
                   }
                   deferred2.isCompleted ->{
                       deferred1.cancel()
                       return@withTimeout deferred2.await()
                   }
                   else->{
                       delay(10)
                   }
               }
           }
            error("not active")
        }
    } catch (e: TimeoutCancellationException){
        deferred1.cancel()
        deferred2.cancel()
        throw e
    }
}