package RxJava.newTasks

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

fun main(){
    val k = Observable.interval(500, TimeUnit.MILLISECONDS)
        .map { index ->
            when (index.toInt()) {
                0 -> "a"
                1 -> "ab"
                2 -> "abc"
                3 -> "abc"
                4 -> "abcd"
                5 -> "a"
                6 -> "abcd"
                7 -> "abcde"
                else -> ""
            }
        }
        .filter { it.length >= 3 }
        .distinctUntilChanged()
        .subscribe { query ->
            println("Поисковый запрос: '$query'")
        }

    Thread.sleep(2500)
}
