package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable

fun main() {
    val observable1 = Observable.just("A", "B", "C")
    val observable2 = Observable.just(1, 2, 3)
    val observable3 = observable1.zipWith(observable2, { obs1, obs2 ->
        "$obs1$obs2"
    })

    observable3.subscribe { println(it) }
}