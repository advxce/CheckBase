package RxJava.easyTasks

import io.reactivex.rxjava3.core.Flowable

fun main() {
    val flowable = Flowable.range(1, 1000)
        .onBackpressureBuffer()
        .take(10)
        .map { it * it }



    flowable.subscribe { println(it) }
}