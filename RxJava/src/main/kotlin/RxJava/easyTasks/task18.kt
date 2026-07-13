package RxJava.easyTasks

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.AsyncSubject
import java.util.concurrent.TimeUnit
import kotlin.concurrent.atomics.AtomicInt
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * Тема: Single, cache, defer
 *
 * Создайте Single, который эмитит текущее время в миллисекундах (System.currentTimeMillis()).
 * Сделайте так, чтобы при первой подписке возвращалось текущее время,
 * а при повторных подписках в течение 3 секунд возвращалось то же самое значение
 * (кэшированное).
 */

fun main() {
//    val single = cacheValueWithInterval(3000)
//
//
//    val sub1 = single.subscribe {
//        println(it)
//    }
//
//    Thread.sleep(2000)
//
//    val sub2 = single.subscribe {
//        println(it)
//    }
//
//    Thread.sleep(2000)
//
//    val sub3 = single.subscribe {
//        println(it)
//    }
//
//
//}
//
//fun cacheValueWithInterval(timeoutMillis: Long): Single<Long> {
//    val cachedValue = AtomicLong(0)
//    val cacheCreationTime = AtomicLong(0)
//
//    return Single.defer {
//        val now = System.currentTimeMillis()
//
//        // Если кэш пустой (0) или истекло время жизни кэша
//        if (now - cacheCreationTime.get() >= timeoutMillis) {
//            val newValue = System.currentTimeMillis()
//            cachedValue.set(newValue)           // Сохраняем новое значение
//            cacheCreationTime.set(now)          // Сохраняем время создания кэша
//            Single.just(newValue)
//        } else {
//            // Возвращаем кэшированное значение
//            Single.just(cachedValue.get())
//        }
//    }


    val subject = AsyncSubject.create<Int>()

    subject.onNext(1)
    subject.onComplete()
    subject.subscribe { println("A: $it") }
    subject.onNext(2)

    subject.subscribe { println("B: $it") }
    subject.onNext(3)


}

//fun cacheValueWithInterval(time: Long): Single<Long> {
//    val cachedValue = AtomicLong(0)
//    val interval = AtomicLong(0)
//    return Single.defer {
//        val currentCachedTime = System.currentTimeMillis()
//        if(interval.get() == 0L || currentCachedTime - interval.get()>=time ){
//            val newValue = System.currentTimeMillis()
//            interval.set(currentCachedTime)
//            cachedValue.set(newValue)
//            Single.just(newValue)
//        } else {
//            Single.just(cachedValue.get())
//        }
//    }
//}