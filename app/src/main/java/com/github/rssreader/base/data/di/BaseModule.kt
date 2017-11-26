package com.github.rssreader.base.data.di

import com.github.rssreader.base.domain.Thread
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class BaseModule {

    companion object {
        const val MAIN_THREAD_INJECTION_ID = "mainThread"
        const val NEW_THREAD_INJECTION_ID = "newThread"
    }

    @Provides
    @Named(MAIN_THREAD_INJECTION_ID)
    fun provideMainThread(): Thread = ThreadImpl(AndroidSchedulers.mainThread())

    @Provides
    @Named(NEW_THREAD_INJECTION_ID)
    fun provideNewThread(): Thread = ThreadImpl(Schedulers.newThread())

    inner class ThreadImpl(val thread: Scheduler) : Thread {
        override fun get(): Scheduler = thread
    }
}
