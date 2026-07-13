package RxJava.testObservableOnAndSubscribeOn

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun main() {
//   Single.just<Int>(1,)
//        .map {
//            println("Thread1: ${Thread.currentThread().name}")
//            it
//        }
//        .observeOn(Schedulers.computation())
//        .map {
//            println("Thread2: ${Thread.currentThread().name}")
//            it
//        }
//        .subscribeOn(Schedulers.io())
//        .map {
//            println("Thread3: ${Thread.currentThread().name}")
//            it
//        }
//        .observeOn(Schedulers.newThread())
//        .map {
//            println("Thread4: ${Thread.currentThread().name}")
//            it
//        }
//        .observeOn(Schedulers.single())
//        .map {
//            println("Thread5: ${Thread.currentThread().name}")
//            it
//        }
//    .subscribe{
//        println("Thread6: ${Thread.currentThread().name}")
//    }


    val test = Observable.just(1,2,3)
        .map {
            println("map1 thread ${Thread.currentThread().name}")
            it*it
        }
        .observeOn(Schedulers.computation())
        .map { println("map2 thread ${Thread.currentThread().name}")
        it+2}
        .subscribeOn(Schedulers.io())
        .map { println("map3 thread ${Thread.currentThread().name}")
        it-2}
        .subscribe {
            println("sub thread ${Thread.currentThread().name}")
            println(it)
        }

    Thread.sleep(1000)
}