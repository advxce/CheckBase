package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {

    val numbers = Observable.interval(300, TimeUnit.MILLISECONDS).take(3)
    val letters = Observable.interval(500, TimeUnit.MILLISECONDS).take(3)

    Observable.combineLatest(numbers, letters) { num, letter ->
        "[$num:$letter]"
    }.subscribe { println(it) }  // [0:0], [1:0], [1:1], [2:1], [2:2]

    networkRequest()

        .retryWhen { errors ->
            errors.zipWith(Observable.range(1, 3)) { error, attempt ->
                println("⚠️ Попытка $attempt: ${error.message}")
                attempt
            }
                .flatMap { attempt ->
                    println("⏳ Ждем 2 секунды перед попыткой $attempt...")
                    Observable.timer(2, TimeUnit.SECONDS)
                }
        }
        .subscribe(
            { result -> println("✅ Успех: $result") },
            { error -> println("❌ Окончательная ошибка: ${error.message}") }
        )

    Thread.sleep(15000)
}

var attemptCount = 0

fun networkRequest(): Observable<String> {
    return Observable.fromCallable {
        attemptCount++
        println("📡 Попытка подключения #$attemptCount")

        if (attemptCount < 4) {  // Первые 3 попытки падают
            throw RuntimeException("Ошибка сети (попытка #$attemptCount)")
        }
        "Данные успешно получены!"
    }.subscribeOn(Schedulers.io())
}