package com.github.rssreader.base.domain

import io.reactivex.Scheduler


interface Thread {

    fun get(): Scheduler

}