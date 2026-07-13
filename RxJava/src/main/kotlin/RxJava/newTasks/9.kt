package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.just(1,2,3,4,5)
        .scan { acc, newVal ->
            println("acc: $acc, newVal $newVal")
            acc * newVal
        }
        .subscribe { println(it) }
}