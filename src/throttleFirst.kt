import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val newFlow = flow<Int> {
        repeat(20) {
            emit(it)
            delay(100)
        }
    }.throttleFirst(500)

    delay(2000)
    newFlow.collect {
        println(it)
    }
}


fun <T> Flow<T>.throttleFirst(time: Long): Flow<T> = flow {

    var startTime = 0L

    collect { value ->
        val currentTime = System.currentTimeMillis()

        if(currentTime - startTime >= time){
            startTime = currentTime
            emit(value)

        }
    }

}
