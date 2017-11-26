package com.github.rssreader.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "http://validator.w3.org/"
    }

    @Provides
    fun provideRetrofit(): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

}
