package RxJava.easyTasks

import io.reactivex.rxjava3.core.Single

fun main(){
    val list = listOf("appl", "bansggs", "chessr", "datfssfsfsfsf")

    val single = Single.create<String>{ emitter->
        if(list.isNotEmpty()){
            var longestString:String = list[0]

            for(i in 1..<list.size){
                if(longestString.length < list[i].length){
                    longestString = list[i]
                }
            }
            emitter.onSuccess(longestString)
        } else {
            emitter.onError(Throwable("Empty List"))
        }
    }

    single.subscribe{
        println(it)
    }
}