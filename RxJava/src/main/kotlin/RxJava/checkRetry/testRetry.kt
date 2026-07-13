package RxJava.checkRetry

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main(){
//    var attempt = 0
//    Observable.create<String> { emitter ->
//        attempt++
//        println("try $attempt")
//        if (attempt < 3) {
//            emitter.onError(RuntimeException("Error #$attempt"))
//        } else {
//            emitter.onNext("Success!")
//            emitter.onComplete()
//        }
//    }
//        .retry(1)  // Максимум 3 попытки
//        .subscribe(
//            { value -> println("get: $value") },
//            { error -> println("final error: $error") }
//        )
//
    var attempt = 0
    Observable.create<String> { emitter ->
        attempt++
        println("Попытка $attempt")
        if (attempt < 4) {
            emitter.onError(RuntimeException("Ошибка #$attempt"))
        } else {
            emitter.onNext("Успех!")
            emitter.onComplete()
        }
    }
        .retryWhen { errors ->
            errors.zipWith(Observable.range(1, 5)) { error, attemptNum ->
                println("Ошибка #$attemptNum, ждем ${attemptNum * 1000}мс")
                attemptNum  // Возвращаем номер попытки
            }
        }
        .subscribe { value -> println("Получено: $value") }
}