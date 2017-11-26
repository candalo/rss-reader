package com.github.rssreader.base.data.di

import com.github.rssreader.base.domain.Thread
import dagger.Component
import javax.inject.Named

@Component(modules = arrayOf(BaseModule::class))
interface BaseComponent {

    @Named(BaseModule.MAIN_THREAD_INJECTION_ID) fun createMainThread(): Thread

    @Named(BaseModule.NEW_THREAD_INJECTION_ID) fun createNewThread(): Thread

}
