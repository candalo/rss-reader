package com.github.rssreader.features.feed.data.repository.datasource

import com.github.rssreader.features.feed.data.entity.FeedEntity
import io.reactivex.Observable
import javax.inject.Inject


class CloudFeedDataSource @Inject constructor(private val restApi: FeedRestApi) {

    fun find(feedUrl: String): Observable<FeedEntity>
            = restApi.getFeed(feedUrl)

}