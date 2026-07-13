//package liveCodding
//
////Написать функцию, которая делает несколько асинхронно запросов
////и в случае ошибки пытается повторить запрос до трех раз.
//
//
//fun main(): Unit = runBlocking {
//    val ids = listOf(1, 2, 3, 4, 5)
//
//    request(ids).forEach{ user->{
//        try{
//            println(user.await())
//        }
//        catch(e:Exception){
//            println(e.message)
//        }
//
//    }
//
//        //"user1”
//        //"user2”
//        //"user3”
//        //ERROR
//        //"user5”
//
//    }
//
//    suspend fun request(list:List<Int>): List<Deffered<String>> = coroutineScope{
//        val resultList = list.map{ id->
//            async{
//                getUserByIdWithRetry(id)
//            }
//        }
//        return resultList
//    }
//
//    suspend fun getUserById(id:Int): String{
//        delay(1000)
//        return if (Random.nextBoolean()) throw RuntimeException("Failed to fetch USER")
//        else "User$id"
//
//    }
//
//    suspend fun getUserByIdWithRetry(id:Int):String{
//        var exception: Exception? = null
//        repeat(4) {
//            try{
//                return getUserById(id)
//            } catch(e :Exception){
//                exception = e
//            }
//        }
//        throw exception ?: Exception("Error")
//        //All attempts failed
//    }