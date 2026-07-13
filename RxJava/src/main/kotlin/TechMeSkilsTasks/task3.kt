package TechMeSkilsTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.range(1,100)
        .skip(50)
        .take(5)

    observable.subscribe {
        println(it)
    }
}