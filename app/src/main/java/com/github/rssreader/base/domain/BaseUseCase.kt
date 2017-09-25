package com.github.rssreader.base.domain

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseUseCase<T, in Params>(private val subscriberThread: Thread,
                                         private val observerThread: Thread
) : UseCase<T, Params> {

    private val disposables = CompositeDisposable()

    protected abstract fun getObservable(params: Params): Observable<T>

    override fun <S> execute(observer: S, params: Params)
            where S : Observer<in T>?,
                  S : Disposable {
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