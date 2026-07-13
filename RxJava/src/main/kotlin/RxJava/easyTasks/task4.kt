package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable

fun main() {
    val observable = Observable.create<Int> { emitter ->
        repeat(20) {
            emitter.onNext(it)
        }
    }
        .filter { it % 2 == 0 }
        .map { it * 10 }

    observable.subscribe{
        println(it)
    }
}