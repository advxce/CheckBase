package RxJava.allObservables

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import java.util.concurrent.TimeUnit

fun main(){
    val observable = Observable.just(1)

    val single = Single.just(1)
        .subscribe(
            {},

        )

    val maybe = Maybe.just(1)
        .subscribe(
            {},
            {},
            {}
        )
    val completable = Completable.complete()
    val flowable = Flowable.just(1)

    val publishSubject = PublishSubject.create<Int>()
    val asyncSubject = AsyncSubject.create<Int>()
    val replaySubject = ReplaySubject.create<Int>()
    val behaviorSubject = BehaviorSubject.create<Int>()
    Observable.just(1, 2, 3,0)
        .map { 10 / it }
        .onErrorReturnItem(-1)
        .subscribe { println("Get: $it") }


    val refCounted = Observable.interval(1, TimeUnit.SECONDS)
        .take(5)
        .publish()
        .refCount()  //
}



//fun loadData(): Observable<Data> {
//    return Observable.concat(
//        getFromCache()  // Сначала пробуем кэш
//            .onErrorResumeNext(Observable.empty()),  // Ошибка кэша игнорируем
//        getFromNetwork()  // Потом сеть
//            .doOnNext { saveToCache(it) }  // Сохраняем в кэш
//    ).firstOrError()  // Берем первый успешный
//        .onErrorReturn { Data.default() }  // Если всё упало - дефолт
//        .toObservable()
//}



//val loadingSubject = BehaviorSubject.createDefault(false)
//
//dataRepository.getData()
//.doOnNext { data ->
//    loadingSubject.onNext(false)  // Выключаем загрузку
//}
//.doOnSubscribe {
//    loadingSubject.onNext(true)   // Включаем загрузку
//}
//.subscribe { data -> showData(data) }