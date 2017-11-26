package com.github.rssreader.network

import dagger.Component
import retrofit2.Retrofit

@Component(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {

    fun createRetrofit(): Retrofit

}
