package com.github.rssreader.base.presentation.presenter


interface Presenter<in T> {

    fun attachTo(view: T)

    fun destroy()

}