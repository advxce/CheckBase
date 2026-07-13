package middleTasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

data class UserEntity(
    val id: String,
    val name: String,
    val isSynced: Boolean
)

interface UserDao {
    suspend fun insertOrUpdate(user: UserEntity)
    suspend fun getUserById(id: String): UserEntity?
}

interface UserApi {
    suspend fun updateProfile(id: String, name: String)
}

class UserRepository(
    private val userDao: UserDao,
    private val userApi: UserApi
) {
    // Твой код должен быть здесь
    // Метод вызывается из ViewModel, когда пользователь нажимает кнопку "Сохранить"
    suspend fun updateUserName(userId: String, newName: String) = withContext(Dispatchers.IO) {
        val currentUser = UserEntity(userId, newName, false)
        userDao.insertOrUpdate(currentUser)
        try {
            userApi.updateProfile(userId, newName)
            userDao.insertOrUpdate(currentUser.copy(isSynced = true))
        } catch (e: Exception) {
            //
        }

    }
}



fun main() = runBlocking {

    var i = 0

    repeat(50000){
       launch {
           i++
       }


    }

    delay(1000)

    println(i)

}
