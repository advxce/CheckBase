package RxJava.test

import io.reactivex.rxjava3.subjects.AsyncSubject

fun main(){

    val publishSubject = AsyncSubject.create<Int>()

    println("start emit")

    publishSubject.onNext(1)
    publishSubject.onNext(10)

    Thread.sleep(300)

    publishSubject.subscribe{
        println("FIRST SUB " + it)
    }
    publishSubject.onNext(2)
    Thread.sleep(500)

    publishSubject.subscribe{
        println("SECOND SUB " + it)
    }

    Thread.sleep(500)
    publishSubject.onNext(3)

    publishSubject.onComplete()
}
