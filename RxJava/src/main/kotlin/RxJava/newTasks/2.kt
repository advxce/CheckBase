package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun main(){
    val list = listOf<String>("ffsafa", "fffffff", "aaaaaaa","hhhhhhhh", "lllllll")

    val observable = Observable.fromIterable(list)
        .flatMap { image->
            Observable.just(image)
                .delay(Random.nextLong(100, 500), TimeUnit.MILLISECONDS)
                .doOnNext { println("Loaded :"+it) }
                .subscribeOn(Schedulers.io())
        }
        .toList()
        .observeOn(Schedulers.single())
        .subscribe {
            println(it)
        }

    Thread.sleep(2000)
}