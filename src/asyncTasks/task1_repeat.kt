package asyncTasks

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield
import kotlin.random.Random

fun main():Unit = runBlocking {
    val result = downloadFirstFile(listOf("first", "second", "third", "fourth", "fifth", "sixth"))
    println(result)
}

suspend fun downloadFirstFile(urls:List<String>):String = supervisorScope {
    val downloadingFiles = urls.map { url ->    //List<Deferred<String>>
        async{
            delay(Random.nextLong(100))
            url
        }
    }

    var result:String = ""

    try{
        while(true){
            val firstFile = downloadingFiles.firstOrNull{ it.isCompleted && !it.isCancelled}
            if(firstFile != null){
                result = firstFile.await()
                break
            }
            delay(5)
        }
    } finally{
        downloadingFiles.forEach{file->
            file.cancel()
        }
    }

    result
}