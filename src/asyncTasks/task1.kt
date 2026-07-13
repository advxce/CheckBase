package asyncTasks

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield
import java.io.File
import kotlin.random.Random

fun main(): Unit = runBlocking<Unit> {
    val result = downloadFirstSuccessful(listOf("first", "second", "third", "fourth", "fifth", "sixth"))
    println(result)
}

suspend fun downloadFirstSuccessful(urls: List<String>): String = supervisorScope {
    val downloadingFiles = urls.map { url ->
        async {
            delay(Random.nextLong(100, 500))
            url
        }
    }

    var result: String? = null

    try {
        while (true) {
            val completed = downloadingFiles.firstOrNull { it.isCompleted && !it.isCancelled }
            if (completed != null) {
                result = completed.await()
                break
            }
            yield()
        }
    } finally {
        downloadingFiles.forEach { it.cancel() }
    }
    return@supervisorScope result
}