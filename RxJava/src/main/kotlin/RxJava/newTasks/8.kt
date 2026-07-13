package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.range(1,20)
        .buffer(3)
        .subscribe {
            println(it)
        }
}