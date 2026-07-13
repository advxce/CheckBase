package RxJava.newTasks

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

val userList = listOf<User>(
    User("Dima", 1),
    User("Andrey", 2),
    User("Oleg", 3),
)

fun main(){
    val observable = Observable.just(1,2,3)
        .flatMap { userId ->
            getUser(userId)
                .onErrorReturn { User("unknown", userId) }
                .toObservable()
        }
        .subscribe { println(it) }

}

data class User(val name: String, val id: Int)

fun getUser(id:Int): Single<User>{
    return Single.fromCallable {
        userList[id]
    }
}