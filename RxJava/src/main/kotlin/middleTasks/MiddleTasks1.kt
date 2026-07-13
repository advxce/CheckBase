package middleTasks
import kotlinx.coroutines.sync.Mutex
/**
 *
 * Задача: «Кэширующий трекер курсов валют» (Crypto Rate Tracker)
 * Легенда
 *
 * У нас есть приложение для отслеживания стоимости криптовалют.
 * Нам нужно реализовать логику репозитория, который запрашивает текущую
 * цену биткоина (BTC) к доллару (USD), кэширует её на короткое время и отдаёт UI-слою.
 * ТЗ и Требования
 *
 *     Запрос в сеть: Нужно вызвать метод сетевого клиента api.fetchBitcoinPrice(): Single<Double>
 *         (или suspend функцию, в зависимости от того, стек какого года вы проверяете.
 *         Пусть в примере будут Coroutines).
 *
 *     Кэширование: Сетевой запрос дорогой. Если с момента последнего
 *     успешного запроса прошло меньше 5 секунд, репозиторий должен вернуть последнее
 *     сохраненное значение из памяти (in-memory cache) без вызова сети. Если прошло больше
 *     5 секунд — нужно снова идти в сеть.
 *
 *     Потокобезопасность (Thread Safety): Метод репозитория может вызываться одновременно
 *     из разных мест приложения и с разных потоков. Нужно исключить состояние гонки (Race Condition).
 *
 *     Обработка ошибок: Если сеть упала, но в кэше есть старые данные (пусть даже старше 5 секунд),
 *     нужно вернуть их. Если кэша нет вообще — пробросить ошибку дальше.
 *
 */



interface CryptoApi {
    suspend fun fetchBitcoinPrice(): Double
}

class CryptoRepository(private val api: CryptoApi) {
    // Твой код должен быть здесь
    var successTime: Long = 0
    var tempData: Double? = null
    val mutex = Mutex()
    suspend fun getBitcoinPrice(): Double {
        mutex.lock()
        try {
            val currentSeconds = System.currentTimeMillis()/1000
            if(currentSeconds - successTime < 5){
                return tempData ?: throw IllegalArgumentException()
            }
            val response = api.fetchBitcoinPrice()
            successTime = currentSeconds
            tempData = response
            return response

        } catch (e: Exception){
            return tempData ?: throw e
        } finally {
            mutex.unlock()
        }

    }
}

//

/**
 * Эталонное решение
 *
 * import kotlinx.coroutines.sync.Mutex
 * import kotlinx.coroutines.sync.withLock
 *
 * class CryptoRepository(private val api: CryptoApi) {
 *
 *     private val mutex = Mutex()
 *
 *     private var cachedPrice: Double? = null
 *     private var lastFetchedTime: Long = 0L
 *     private val cacheTimeout = 5000L // 5 секунд
 *
 *     suspend fun getBitcoinPrice(): Double {
 *         return mutex.withLock {
 *             val currentTime = System.currentTimeMillis() // В Android лучше SystemClock.elapsedRealtime()
 *
 *             // Проверяем валидность кэша
 *             if (cachedPrice != null && (currentTime - lastFetchedTime < cacheTimeout)) {
 *                 return@withLock cachedPrice!!
 *             }
 *
 *             try {
 *                 // Идем в сеть, если кэш устарел или отсутствует
 *                 val freshPrice = api.fetchBitcoinPrice()
 *                 cachedPrice = freshPrice
 *                 lastFetchedTime = currentTime
 *                 freshPrice
 *             } catch (e: Exception) {
 *                 // Если сеть упала, но есть хоть какой-то кэш — отдаем его
 *                 cachedPrice ?: throw e
 *             }
 *         }
 *     }
 * }
 *
 *
 */