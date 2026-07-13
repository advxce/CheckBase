import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    coroutineScope {
        try {
            val deferred = async {
                println("async")
                throw IllegalArgumentException("exception")
            }
            deferred.await()
        } catch (e: Exception){
            
            println("exception catch")
        }

        launch {
            println("launch work")
        }
    }
}