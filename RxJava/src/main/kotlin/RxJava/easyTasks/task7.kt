package RxJava.easyTasks

import io.reactivex.rxjava3.core.Completable
import java.io.IOException

fun main(){

    saveData().subscribe (
        {
            println("Save")
        },
        {
            error -> println(error.message)
        }
    )

    Thread.sleep(4000)

}

fun saveData(): Completable{
    return Completable.create { emitter ->

        println("Connect with db")
        Thread.sleep(4000)
        println("data save to db")
        emitter.onError(Throwable("Cant save data"))
    }
}