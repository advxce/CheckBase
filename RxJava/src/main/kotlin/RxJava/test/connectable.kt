package RxJava.test

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main(){
    val observable = Observable.interval(1, TimeUnit.SECONDS)
    val connectable = observable.publish()

    connectable.subscribe {
        println(it)
    }
    connectable.connect()
    Thread.sleep(1000)
    connectable.subscribe {
        println("second "+it)
    }

    Thread.sleep(10000)
}