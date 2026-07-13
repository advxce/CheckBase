package middleTasks

/**
 *
 * Задача: «Поисковый движок с защитой от дребезга» (Debounce Search)
 * Легенда
 *
 * Мы пишем экран поиска товаров. Пользователь быстро вводит текст в строку поиска (EditText).
 * Если отправлять запрос на сервер при каждом нажатии символа, мы просто положим наш бэкап спамом.
 * Нужно реализовать логику, которая ждет, пока пользователь сделает паузу, и только потом отправляет запрос.
 * ТЗ и Требования
 *
 *     Эмиссия данных: У нас есть текстовый поток от UI, представленный в виде Kotlin
 *     MutableStateFlow<String> (или PublishSubject в RxJava).
 *
 *     Защита от спама (Debounce): Запрос к API должен уходить только тогда,
 *     когда пользователь перестал вводить текст на протяжении 400 миллисекунд.
 *
 *     Фильтрация: Не нужно отправлять пустые запросы или запросы короче 2 символов.
 *
 *     Игнорирование дубликатов: Если пользователь ввел "Кот", подождал, стер "т" и снова
 *     быстро написал "т" (получилось опять "Кот") — повторный запрос на сервер идти не должен.
 *
 *     Асинхронность (Threading): Ввод текста происходит на Main-потоке, сам сетевой запрос
 *     должен выполняться на Background-потоке (Dispatchers.IO), а результат поиска должен
 *     прилетать обратно на Main-поток для отображения в UI.
 *
 *     Жизненный цикл (Lifecycle): Нужно учесть, что если пользователь закроет экран во время
 *     ожидания или выполнения запроса, всё должно вовремя отмениться, чтобы не было утечки памяти.
 *
 */


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

interface SearchApi {
    suspend fun searchProducts(query: String): List<String>
}

class SearchViewModel(private val api: SearchApi) {
    // Сюда UI-слой (Fragment/Compose) передает каждый измененный символ
    val searchQuery = MutableStateFlow("")
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    // Твой код должен быть здесь
    // Нужно подписаться на searchQuery, обработать его по ТЗ и вызвать api.searchProducts()
    val searchResult = searchQuery
        .debounce(400.milliseconds)
        .filter { it.length >= 2 }
        .distinctUntilChanged()
        .mapLatest {
            withContext(Dispatchers.IO) {
                try {
                    api.searchProducts(it)
                } catch (e: Exception){
                    emptyList()
                }

            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    fun onCleared(){
        viewModelScope.cancel()
    }

}


/**
 *
 *
 * Идеальный код, но я бы предпочел с Flow
 *
 * class SearchViewModel(private val api: SearchApi) : ViewModel() {
 *
 *     val searchQuery = MutableStateFlow("")
 *
 *     // Сюда UI будет подписываться для получения результатов
 *     private val _searchResults = MutableStateFlow<List<String>>(emptyList())
 *     val searchResults: StateFlow<List<String>> = _searchResults
 *
 *     @OptIn(FlowPreview::class)
 *     fun initSearchPipeline() {
 *         // Запускаем цепочку в рамках жизненного цикла ViewModel
 *         viewModelScope.launch {
 *             searchQuery
 *                 .debounce(400) // Ждем 400 мс затишья
 *                 .filter { query -> query.length >= 2 } // Игнорируем строки меньше 2 символов
 *                 .distinctUntilChanged() // Не ищем то же самое повторно
 *                 .flowOn(Dispatchers.Default) // Оптимально для тяжелых операторов вроде debounce
 *                 .mapLatest { query ->
 *                     // mapLatest автоматически отменит старый сетевой запрос,
 *                     // если пользователь возобновил ввод, не дождавшись ответа сервера
 *                     try {
 *                         api.searchProducts(query)
 *                     } catch (e: Exception) {
 *                         emptyList() // Обработка ошибок сети
 *                     }
 *                 }
 *                 .flowOn(Dispatchers.IO) // Сетевой запрос выполняем на IO-диспетчере
 *                 .collect { results ->
 *                     _searchResults.value = results // Возвращаемся на Main (viewModelScope по умолчанию на Main)
 *                 }
 *         }
 *     }
 * }
 *
 */