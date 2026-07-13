package RxJava.easyTasks

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

class User(private val name:String){
    fun join(channel: PublishSubject<String>): Disposable{
        return channel.subscribe(
            {message -> println("User $name get the $message")},
            {error -> println(error.message)},
            {println("user $name left the chat")}
        )
    }
}

fun main(){
    val channel = PublishSubject.create<String>()
    val user1 = User("Dima")
    val user2 = User("Andrey")
    val user1Sub = user1.join(channel)
    println("user1 join")
    Thread.sleep(1000)
    channel.onNext("hello")
    Thread.sleep(2000)
    channel.onNext("world")
    Thread.sleep(2000)

    val user2Sub = user2.join(channel)
    println("User2 joined")
    Thread.sleep(2000)
    channel.onNext("hello from user2")
    Thread.sleep(2000)
    channel.onNext("world from user2")
    user2Sub.dispose()
    Thread.sleep(2000)
    channel.onNext("user2 hello why are you dont answer me")
    Thread.sleep(2000)
    channel.onNext("ayyy")
    Thread.sleep(2000)
    println("chat finished")
    channel.onComplete()

}