package TechMeSkilsTasks

import io.reactivex.rxjava3.core.Observable

fun main(){
    val observable = Observable.range(1,20).filter{it%2==0}
        .map{it*it*it}


    observable.subscribe{
        println(it)
    }
}