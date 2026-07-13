package asyncTasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main():Unit = runBlocking {


    val flow = flow {
        repeat(6){ num->
            emit(num)
            delay(500)
        }
    }


    val subscribe1 = launch {
        flow.collect {
            println("first collect")
            println(it)
        }

    }


    delay(1000)


    val subscribe2 = launch {
        flow.collect {
            println("second collect")
            println(it)
        }
    }
//    flow.collect {
//        println("second collect")
//        println(it)
//    }


    delay(10000)
    subscribe1.cancel()
    subscribe2.cancel()

}