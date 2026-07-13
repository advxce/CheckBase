package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main() {
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val observable = Observable.fromIterable(list)
        .concatMap { number ->
//            Observable.interval(500, TimeUnit.MILLISECONDS)
//                .take(1)
//                .map { number }
            Observable.just(number)
                .delay(500, TimeUnit.MILLISECONDS)
        }
        .subscribe {
            println(it)
        }

    Thread.sleep(9000)
}