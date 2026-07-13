package RxJava.easyTasks

import io.reactivex.rxjava3.core.Maybe

fun main(){
    findPhoneByName("Dima").subscribe {
        println(it)
    }
}

fun findPhoneByName(name:String): Maybe<String>{
    val users = mapOf<String, Int>("Dima" to 2222, "Andrey" to 3333)

    val maybe = Maybe.create<String>{ emitter->
        val findUser = users.keys.find { it == name }
        if(findUser!=null){
            emitter.onSuccess("$findUser ${users[findUser]}")
        } else {
            emitter.onComplete()
        }

    }

    return maybe
}