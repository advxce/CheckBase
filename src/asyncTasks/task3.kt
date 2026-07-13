package asyncTasks

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.math.min
import kotlin.random.Random

fun main():Unit = runBlocking {
    retryWithExponentialBackoff {
        println(getUserById(2))
    }
}

suspend fun getUserById(id:Int): String{
    delay(1000)
    return if (Random.nextBoolean()) throw RuntimeException("Failed to fetch USER")
    else "User$id"

}

suspend fun <T> retryWithExponentialBackoff(
    maxAttempts:Int = 3,
    initialDelay: Long = 100,
    maxDelay: Long = 5000,
    block: suspend () -> T
) = coroutineScope {
   repeat(maxAttempts+1){ attempt->
       try {
           val delay = min(initialDelay * Math.pow(2.0, (attempt-1).toDouble()), maxDelay.toDouble())
           delay(delay.toLong())
           return@coroutineScope block()

       } catch (e:RuntimeException){
           println("Exception $e")
       }
   }
}