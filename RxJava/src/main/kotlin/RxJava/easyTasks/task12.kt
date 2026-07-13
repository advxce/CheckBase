package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.just<String>("hello", "world", "rx","java")
        .map { it.length }
        .filter { it > 3 }

    observable.subscribe { println(it) }
}