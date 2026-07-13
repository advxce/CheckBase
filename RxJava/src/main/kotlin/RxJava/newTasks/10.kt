package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val age = Observable.just(1, 2, 3, 5)
    val name = Observable.just("Dima", "Andrey", "Vika")

    name.zipWith(age){ name, age ->
        "Name: $name, Age: $age"
    }.subscribe {
        println(it)
    }
}
