package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable

/**
 * Тема: Observable.fromIterable, filter, firstElement
 *
 * Дан список чисел от 1 до 20. Найдите первое число, которое делится и на 3, и на 5.
 * Если такое число найдено, выведите его, иначе выведите сообщение "Число не найдено".
 */

fun main(){
    val numbers = (1..10).toList()
    val firstNumber = Observable.fromIterable(numbers)
        .filter { it%3 == 0 && it %5 == 0 }
        .firstOrError()
        .subscribe(
            { println("First Element: $it") },
            { println("number not found") },

        )

    Thread.sleep(2000)
    firstNumber.dispose()
}