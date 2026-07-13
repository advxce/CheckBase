package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main(){
    val userInput = Observable.just(
        "k",           // быстрый ввод
        "ko",          // без паузы
        "kot",         // без паузы
        "koti",        // маленькая пауза 300мс (меньше 500 - игнорируется)
        "kotic"        // пауза 600мс после "коти" - сработает debounce
    ).concatMap {
        Observable.just(it)
            .delay(300, TimeUnit.MILLISECONDS)
    }

    userInput
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMap{query->
            searchApi(query)
                .subscribeOn(Schedulers.io())
        }
        .subscribe {
            println(it)
        }

    Thread.sleep(5000)
}
fun searchApi(query: String): Observable<String> {
    return Observable.fromCallable {
        println("🔍 Make request for: '$query'")
        Thread.sleep(3)  // Имитация времени ответа API
        "Result for '$query'"
    }
}