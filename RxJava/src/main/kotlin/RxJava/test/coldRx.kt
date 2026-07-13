package RxJava.test

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import java.io.IOException

fun main(){
    val cold = Observable.never<Int>()

    Thread.sleep(1000)

    cold.subscribe{
        println("First "+it)
    }

    Thread.sleep(1000)
}