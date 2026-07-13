package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main(){
    val observable = Observable.interval(200, TimeUnit.MILLISECONDS)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .subscribe { println("Interval is $it") }


    Thread.sleep(3000)
}


