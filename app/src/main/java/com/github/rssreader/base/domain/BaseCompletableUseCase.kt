package com.github.rssreader.base.domain

import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseCompletableUseCase<in Params>(private val subscriberThread: Thread,
                                                        private val observerThread: Thread
) : CompletableUseCase<Params>  {

    private val disposables = CompositeDisposable()

    protected abstract fun getCompletable(params: Params): Completable

    override fun <S> execute(observer: S, params: Params)
            where S : CompletableObserver,
                  S : Disposable {
        val completable = getCompletable(params)
                .subscribeOn(subscriberThread.get())
                .observeOn(observerThread.get())
        disposables.add(completable.subscribeWith(observer))
    }

    override fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}