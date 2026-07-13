package RxJava.easyTasks

import io.reactivex.rxjava3.core.Maybe

fun main(){

    val list = listOf(1,4,3,2,1)
        .find { it>3 }
        ?.let {
            Maybe.just(it)
        }
        ?: Maybe.empty()

//    val maybe = Maybe.create<Int> { emitter ->
//        val result = list.find { it>3 }
//        if(result!=null){
//            emitter.onSuccess(result)
//        } else {
//            emitter.onComplete()
//        }
//    }

    val check = list.subscribe{
        println(it)
    }
}










