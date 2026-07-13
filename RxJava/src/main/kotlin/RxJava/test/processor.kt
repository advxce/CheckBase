package RxJava.test

import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.PublishProcessor

fun main(){
    val processor = BehaviorProcessor.create<Int>()

    processor.onNext(1)
    processor.onNext(2)

    processor.subscribe{
        println("first "+it)
    }

    Thread.sleep(1000)
    processor.onNext(3)
    processor.onNext(4)

    processor.subscribe{
        println("second "+it)
    }

}