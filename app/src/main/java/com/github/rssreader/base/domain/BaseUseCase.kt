package com.github.rssreader.base.domain

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver


abstract class BaseUseCase<T, in Params>(private val subscriberThread: Thread,
                                         private val observerThread: Thread) : UseCase<T, Params> {

    private val disposables = CompositeDisposable()

    protected abstract fun getObservable(params: Params): Observable<T>

    override fun execute(observer: DisposableObserver<T>, params: Params) {
        val observable = getObservable(params)
                .subscribeOn(subscriberThread.get())
                .observeOn(observerThread.get())
        disposables.add(observable.subscribeWith(observer))
    }

    override fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}