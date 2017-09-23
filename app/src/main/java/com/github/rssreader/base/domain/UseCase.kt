package com.github.rssreader.base.domain

import io.reactivex.observers.DisposableObserver


interface UseCase<T, in Params> {

    fun execute(observer: DisposableObserver<T>, params: Params)

    fun dispose()

}