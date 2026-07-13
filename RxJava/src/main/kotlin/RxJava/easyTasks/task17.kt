package RxJava.easyTasks

import io.reactivex.rxjava3.core.Observable

/**
 * Тема: Observable.zip, just, fromCallable
 *
 * У вас есть два источника:
 *
 *     Список имён: ["Анна", "Борис", "Виктор"]
 *
 *     Список возрастов: [25, 30, 35]
 *     Объедините их в список строк вида "Имя: Анна, Возраст: 25"
 */


fun main() {
    val names = listOf("Hanna", "Boris", "Victor")
    val ages = listOf(25, 30, 35)

    val regex =
        Observable.zip(
            Observable.fromIterable(names),
            Observable.fromIterable(ages)
        ) { name, ages ->
            "$name $ages"
        }
            .subscribe { println(it) }
}