package RxJava.liveCodding

import io.reactivex.rxjava3.core.Observable

fun main() {

    val badWords = listOf("badword", "xxxxx", "not good")

    val result = chatStream()
        .filter { chatMessage ->
            val isBadWordFound = badWords.any { badWord -> chatMessage.content.contains(badWord, true) }
            if (isBadWordFound) {
                println( "BanWords"+ chatMessage.toString())
                //BAN LOGIC
            }
            !isBadWordFound
        }.subscribe{
            println(it)
        }

}


data class ChatMessage(val user: String, val content: String)

fun chatStream(): Observable<ChatMessage> {
    return Observable.create { emitter ->
        emitter.onNext(ChatMessage("User1", "Hello"))
        emitter.onNext(ChatMessage("User2", "Hello, everyone, XXxxx!"))
        emitter.onNext(ChatMessage("User3", "This is a badword message!"))
        emitter.onNext(ChatMessage("User4", "How are you, not gOOd people?"))
        emitter.onNext(ChatMessage("User5", "Oops, sorry for the bad language!"))
        emitter.onComplete()
    }
}