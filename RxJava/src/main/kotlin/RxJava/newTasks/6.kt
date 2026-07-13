package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.just(1,1,2,2,3,2)
        .distinct()
        .subscribe{
            println(it)
        }
}