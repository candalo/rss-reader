package com.github.rssreader.base.domain

import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


interface CompletableUseCase<in Params> {

    fun <S> execute(observer: S, params: Params) where S : CompletableObserver, S : Disposable

    fun dispose()

}