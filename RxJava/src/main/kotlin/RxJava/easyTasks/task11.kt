package RxJava.easyTasks

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun main() {
    val single = Single.create<String> { emitter ->
        Thread.sleep(5000)
        println("task success")
        emitter.onSuccess("Result")
    }

    single
        .timeout(3000, TimeUnit.MILLISECONDS)
        .doOnError { error ->
            if (error is TimeoutException) {
                println("Some exception")
            } else {
                println("Some error")
            }
        }
        .subscribe {
            println(it)
        }

    Thread.sleep(6000)

}