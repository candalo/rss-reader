package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface FeedSubscriptionRestApi {
    @GET("feed/check.cgi")
    fun subscribe(@Query("url") feedUrl: String, @Query("output") outputFormat: String = "soap12"): Observable<Response<ResponseBody>>
}
