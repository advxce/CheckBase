import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val scope= CoroutineScope(SupervisorJob() + Dispatchers.Default)

        scope.launch {
            delay(1000)
            println("1")

        }
        scope.launch {
            println("2")
        throw IllegalArgumentException("fdfsf")
        }
        delay(1000)



    delay(3000)
}