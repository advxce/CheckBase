package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * Тема: Observable.interval, take, map
 *
 * Создайте таймер, который каждую секунду генерирует
 * сообщение "Прошло X секунд". Ограничьте количество сообщений до 5
 */

fun main(){
    val timer = Observable.interval(1, TimeUnit.SECONDS)
        .take(5)
        .subscribe{
            println("Past $it seconds")
        }

    Thread.sleep(6000)
    timer.dispose()
}