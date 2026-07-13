package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main() {
    // Имитация быстрых событий (каждые 100 мс)
    Observable.interval(100, TimeUnit.MILLISECONDS)
        .map { "Event $it" }
        .take(15)
        .doOnNext { println("📢 Generation: $it") }
        .throttleFirst(300, TimeUnit.MILLISECONDS)  // Первое событие в окне
        .subscribe { event ->
            println("✅ Miss: $event")
        }

    Thread.sleep(2000)
}

//fun main() {
//    Observable.interval(100, TimeUnit.MILLISECONDS)
//        .map { "Event $it" }
//        .take(15)
//        .doOnNext { println("📢 Generation: $it") }
//        .sample(200, TimeUnit.MILLISECONDS)  // Последнее событие в окне 200мс
//        .subscribe { event ->
//            println("✅ Miss (last): $event")
//        }
//
//    Thread.sleep(2000)
//}