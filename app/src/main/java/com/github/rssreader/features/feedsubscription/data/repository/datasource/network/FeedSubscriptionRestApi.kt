package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import com.github.rssreader.features.feed.data.entity.FeedEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface FeedSubscriptionRestApi {
    @GET
    fun subscribe(@Url url: String): Observable<FeedEntity>
}