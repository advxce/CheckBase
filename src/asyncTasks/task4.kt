package asyncTasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalTime
import java.util.concurrent.atomic.AtomicInteger

fun main(): Unit = runBlocking {
    val limiter = RateLimiter(10)
    repeat(30) {
        limiter.execute {
            log("✅ Executing task $it")
        }
    }
}

fun log(msg: String) {
    val time = LocalTime.now().toString().substring(0, 12)
    println("[$time] $msg")
}

class RateLimiter(private val permitsPerSecond: Int) {

    private var rateLimiter = 0
    private val mutex = Mutex()

    suspend fun <T> execute(block: suspend () -> T): T {
        while (true) {
            var isDelay = false
            mutex.withLock {
                if (rateLimiter < permitsPerSecond) {
                    rateLimiter++
                    log("💡 rateLimiter increment: $rateLimiter / $permitsPerSecond")
                    return block()
                } else {
                    log("🚫 Limit reached ($rateLimiter). Need delay...")
                    isDelay = true
                }
            }

            if (isDelay) {
                log("⏳ Waiting 1 second... (${Thread.currentThread().name})")
                delay(1000)
                rateLimiter = 0
                log("🔄 Reset rateLimiter -> $rateLimiter")
            }
        }
    }
}