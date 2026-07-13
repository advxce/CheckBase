import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main():Unit = runBlocking {

//    checkStateFlow()
//    checkSharedFlow()
//checkEventWithSharedFlow()
    checkEventWithStateFlow()


}

suspend fun checkStateFlow() = coroutineScope {

    val stateFlow = MutableStateFlow<Int>(0)

    val job = launch {
        stateFlow.collect{
            println("stateFlow: $it")
            delay(200)
        }
    }

    repeat(5){ i->
        stateFlow.value = i
        delay(300)
    }


    delay(4000)
    job.cancel()

}

suspend fun checkSharedFlow() = coroutineScope {

    val sharedFlow = MutableSharedFlow<Int>(replay = 2)

    delay(100)

    repeat(5)  {i->
        sharedFlow.emit(i)
        println("emit")
        delay(500)
    }

    delay(1000)

    val job = launch {
        sharedFlow.collect {
            println("collect")
            println("sharedFlow: $it")

//        delay(300)
        }
    }

    delay(4000)
    job.cancel()

    println("suspend check")


}


suspend fun checkEventWithSharedFlow() = coroutineScope {
    val eventFlow = MutableSharedFlow<CustomEvent>()
    var count = 0

    val job = launch {
        eventFlow.collect {
            count++
            println("sharedFlow: $it   count: $count")
        }
    }

    delay(100)

    repeat(5){
        eventFlow.emit(CustomEvent.Start)
    }

    delay(4000)
    job.cancel()


}


suspend fun checkEventWithStateFlow() = coroutineScope {
    val eventFlow = MutableStateFlow<CustomEvent>(CustomEvent.Waiting)
    var count = 0

    val job = launch {
        eventFlow.collect {
            count++
            println("sharedFlow: $it   count: $count")
        }
    }

    delay(100)

    repeat(5){
        eventFlow.value = CustomEvent.Start
    }

    delay(4000)
    job.cancel()


}

sealed class CustomEvent {
    object Start : CustomEvent()
    object Stop : CustomEvent()
    object Waiting : CustomEvent()
}