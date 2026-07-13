package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable

fun main(){

    val list = listOf("Apple", "Banana", "Orange", "Kiwi")

    val observable = Observable.fromIterable(list)

    observable.subscribe{
        println("Fruit $it")
    }
}