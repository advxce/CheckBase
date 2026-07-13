package RxJava.easyTasks

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun main(){

    val city = "Min"
    val single = Single.create<Double>{emitter->
        val temperature = getTemperature(city)

        if(temperature!=null){
            emitter.onSuccess(temperature)
        } else {
            emitter.onError(Throwable("City not found"))
        }

    }

    single.subscribe{
        println(it)
    }

}

fun getTemperature(city:String): Double?{
    return  if(city=="Minsk"){
        37.0
    } else {
        null
    }
}