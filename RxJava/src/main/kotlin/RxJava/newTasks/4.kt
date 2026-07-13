package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.range(1, 50)
        .skip(5)
        .take(3)
        .subscribe{
            println("sub1 :$it")
        }
}