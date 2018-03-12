package com.github.rssreader.base.presentation.view

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager


class RssReaderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDBFlow()
    }

    private fun setupDBFlow() {
        FlowManager.init(this)
    }
}