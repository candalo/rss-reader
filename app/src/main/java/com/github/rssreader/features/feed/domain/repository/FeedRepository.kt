package com.github.rssreader.features.feed.domain.repository

import com.github.rssreader.features.feed.domain.models.Feed
import io.reactivex.Observable


interface FeedRepository {

    fun find(feedUrl: String) : Observable<Feed>

}