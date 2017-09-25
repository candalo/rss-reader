package com.github.rssreader.base.domain

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

interface UseCase<out T, in Params> {

    fun <S> execute(observer: S, params: Params) where S : Observer<in T>?, S : Disposable

    fun dispose()

}