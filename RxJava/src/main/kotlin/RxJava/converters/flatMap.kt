package RxJava.converters

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun main(){

//    Observable.timer(10, TimeUnit.MILLISECONDS, Schedulers.newThread())
//        .subscribeOn(Schedulers.io())
//        .map {
//            println("mapThread: ${Thread.currentThread().name}")
//        }
//        .doOnSubscribe {
//            println("doOnSubscribe: ${Thread.currentThread().name}")
//        }
//        .subscribeOn(Schedulers.computation())
//        .observeOn(Schedulers.single())
//        .flatMap {
//            println("flatMapThread: ${Thread.currentThread().name}")
//            Observable.just(it)
//                .subscribeOn(Schedulers.io())
//        }
//        .subscribe {
//            println("subscribeThread: ${Thread.currentThread().name}")
//        }
//    Thread.sleep(2000)
    tx3()
}

/**
 * mapThread   computation
 *
 * doOnSubscribe newThread
 * flatMapThread single
 * subscribeThread io
 */


fun tx1(){
    Observable.fromCallable {
        println("fromCallable: ${Thread.currentThread().name}")
        "Hello"
    }
        .subscribeOn(Schedulers.io())
        .map {
            println("map: ${Thread.currentThread().name}")
            it.length
        }
        .subscribeOn(Schedulers.computation())
        .doOnNext {
            println("doOnNext: ${Thread.currentThread().name}")
        }
        .subscribe {
            println("subscribe: ${Thread.currentThread().name}")
        }
    Thread.sleep(1000)
}

/**
 *  io
 *  io
 *  io
 *  io
 */

fun tx2(){
    Observable.range(1, 3)
        .subscribeOn(Schedulers.io())
        .map {
            println("map1: ${Thread.currentThread().name}")
            it * 2
        }
        .observeOn(Schedulers.computation())
        .map {
            println("map2: ${Thread.currentThread().name}")
            it + 1
        }
        .doOnSubscribe {
            println("doOnSubscribe: ${Thread.currentThread().name}")
        }
//        .subscribeOn(Schedulers.newThread())
        .subscribe {
            println("subscribe: ${Thread.currentThread().name}")
        }
    Thread.sleep(1000)
}
/**
 * newThread
 * newThread
 * computation
 * computation
 */

fun tx3(){
    Observable.just(1, 2, 3)
        .map {
            println("map0: ${Thread.currentThread().name}")
            it
        }
        .subscribeOn(Schedulers.io())
        .map {
            println("map10: ${Thread.currentThread().name}")
            it
        }
        .concatMap { number ->
            Observable.range(number, 2)
                .subscribeOn(Schedulers.computation())
                .map {
                    println("inner map: ${Thread.currentThread().name} - $it")
                    it
                }
        }
        .map{
            println("map1: ${Thread.currentThread().name} - $it")
            it
        }
        .observeOn(Schedulers.single())
        .subscribe {
            println("subscribe: ${Thread.currentThread().name} - $it")
        }
    Thread.sleep(2000)
}
/**
 *  1 -  1
 *  1 -2
 *
 *
*/

fun tx4(){
    Observable.defer {
        println("defer: ${Thread.currentThread().name}")
        Observable.just("Deferred")
    }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map {
            println("map: ${Thread.currentThread().name}")
            it.length
        }
        .subscribeOn(Schedulers.newThread())  // обратите внимание - после observeOn
        .doOnNext {
            println("doOnNext: ${Thread.currentThread().name}")
        }
        .subscribe {
            println("subscribe: ${Thread.currentThread().name}")
        }
    Thread.sleep(1000)
}

/**
 * io
 * computation
 * computation
 * computation
 */

fun tx5(){
    Observable.interval(100, TimeUnit.MILLISECONDS, Schedulers.trampoline())
        .take(3)
        .subscribeOn(Schedulers.io())
        .map {
            println("map1: ${Thread.currentThread().name} - $it")
            it
        }
        .observeOn(Schedulers.computation())
        .map {
            println("map2: ${Thread.currentThread().name} - $it")
            it * 2
        }
        .delay(50, TimeUnit.MILLISECONDS, Schedulers.single())
        .doOnNext {
            println("doOnNext: ${Thread.currentThread().name} - $it")
        }
        .subscribeOn(Schedulers.newThread())
        .subscribe {
            println("subscribe: ${Thread.currentThread().name} - $it")
        }
    Thread.sleep(1000)
}

/**
 * trampoline
 * computation
 * computation
 * computation
 */

fun tx6(){
    Observable.create<String> { emitter ->
        println("create: ${Thread.currentThread().name}")
        emitter.onNext("Data")
        emitter.onError(RuntimeException("Oops!"))
    }
        .subscribeOn(Schedulers.io())
        .onErrorReturn {
            println("onErrorReturn: ${Thread.currentThread().name}")
            "Recovered"
        }
        .observeOn(Schedulers.computation())
        .map {
            println("map: ${Thread.currentThread().name}")
            it.length
        }
        .retry(1)
        .doOnSubscribe {
            println("doOnSubscribe: ${Thread.currentThread().name}")
        }
        .subscribe(
            { println("onNext: ${Thread.currentThread().name} - $it") },
            { println("onError: ${Thread.currentThread().name} - ${it.message}") }
        )
    Thread.sleep(2000)
}

/**
 * io
 * computation
 * computation
 * computation
 */