package RxJava.easyTasks

import io.reactivex.rxjava3.core.Single

/**
 * Тема: Single, onErrorReturn, map
 *
 * Создайте функцию validatePassword(password: String): Single<Boolean>, которая проверяет:
 *
 *     Длина пароля должна быть не менее 8 символов
 *
 *     Должна быть хотя бы одна цифра
 *     Если проверка не пройдена, возвращайте false (через onErrorReturn)
 */

fun main(){
    val checkPassword = validatePassword("rgrregregre1")
        .subscribe {
            println(it)
        }

}


fun validatePassword(password: String): Single<Boolean> {
    return Single.just(password)
        .map { it.any { c -> c.isDigit() } && it.length >=8 }
        .onErrorReturn { false }
}