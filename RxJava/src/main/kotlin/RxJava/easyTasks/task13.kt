package RxJava.easyTasks

import io.reactivex.rxjava3.subjects.BehaviorSubject
import jdk.jfr.snippets.Snippets

fun main(){

    val subject = BehaviorSubject.createDefault(20)



    val user1 = subject.subscribe { println("first" +it) }
    updateTemperature(subject, 22)
    updateTemperature(subject, 26)
    Thread.sleep(2000)
    updateTemperature(subject, 29)
    val user2 = subject.subscribe { println("second" +it) }
    updateTemperature(subject, 23)
    updateTemperature(subject, 22)


}


fun updateTemperature(subject: BehaviorSubject<Int>, temperature: Int) {
    subject.onNext(temperature)
}