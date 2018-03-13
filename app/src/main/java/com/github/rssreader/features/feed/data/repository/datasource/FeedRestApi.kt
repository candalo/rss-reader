package com.github.rssreader.features.feed.data.repository.datasource

import com.github.rssreader.features.feed.data.entity.FeedEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface FeedRestApi {
    @GET
    fun getFeed(@Url feedUrl: String): Observable<FeedEntity>
}