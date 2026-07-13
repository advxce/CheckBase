package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

fun main(){
    val observable = Observable.interval(1, TimeUnit.SECONDS)
        .doOnDispose { println("Disposed") }
        .share()

    val d1 = observable.subscribe {
        println("sub1 :$it")
    }
    Thread.sleep(2000)

    val d2 = observable.subscribe {
        println("sub2 :$it")
    }
    Thread.sleep(1000)

    val d3 = observable.subscribe {
        println("sub3 :$it")
    }
    Thread.sleep(1000)
    d1.dispose()

    Thread.sleep(2000)
    d2.dispose()
    Thread.sleep(2000)
    d3.dispose()

    Thread.sleep(1000)
    val d4 = observable.subscribe {
        println("sub4 :$it")
    }
    Thread.sleep(2000)
    d4.dispose()
}