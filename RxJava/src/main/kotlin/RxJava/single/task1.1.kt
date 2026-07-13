package RxJava.single

import io.reactivex.rxjava3.core.Single

fun main(){
    val a =5
    val b = 0
    val delimiter = "0"
    val single = Single.create<Int> { emitter ->
        when(delimiter){
            "*"->{
                emitter.onSuccess(a*b)
            }
            "/"->{
                if(b == 0) {
                    emitter.onError(Throwable("Division by zero"))
                    return@create
                }
                emitter.onSuccess(a / b)

            }
            "+"->{
                emitter.onSuccess(a + b)
            }
            "-"->{
                emitter.onSuccess(a - b)
            }
            else -> {
                emitter.onError(Throwable("not found delimiter"))
            }
        }
    }

    single.subscribe{
        println(it)
    }
}