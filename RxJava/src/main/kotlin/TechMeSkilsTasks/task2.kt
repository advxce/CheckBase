package TechMeSkilsTasks

import io.reactivex.rxjava3.core.Observable
import kotlin.properties.Delegates.observable

fun main(){

    val name = listOf("Dima", "Andrey", "Vanya", "Ilya")

    val studentsName = Observable.fromIterable(name)
        .flatMap { name ->
           getScores(name).map { scores ->
               "$scores - $name"
           }
        }

    studentsName.subscribe{
        println(it)
    }

}

fun getScores(name:String):Observable<Int> {
    return when(name){
        "Dima" -> Observable.just(1,2)
        "Andrey" -> Observable.just(10,8)
        "Vanya" -> Observable.just(5,4)
        else -> Observable.just(1,1)
    }
}