package RxJava

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun main() {
    val observable = Observable.just(1, 2, 3)
    observable.subscribe {
        println(it)
    }

    val flowable = Flowable.just(1, 2, 3)

    flowable.onBackpressureDrop()
        .subscribe {
            println(it)
        }



    val single = Single.just(1)
    single.subscribe {
        println(it)
    }
}