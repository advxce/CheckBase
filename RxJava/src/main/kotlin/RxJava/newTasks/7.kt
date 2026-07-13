package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import kotlin.properties.Delegates.observable

fun main(){
    val observable = Observable.just(
        "aaaaaaaaa",
        "bbbb",
        "ccccccc",
        "hh",
        "lllll"
    ).filter{
        it.length>5
    }.map { it.length }
        .subscribe { println(it) }
}