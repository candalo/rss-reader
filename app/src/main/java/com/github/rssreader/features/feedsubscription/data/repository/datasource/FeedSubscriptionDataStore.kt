package com.github.rssreader.features.feedsubscription.data.repository.datasource

import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import io.reactivex.Observable


interface FeedSubscriptionDataStore {

    fun create(feedSubscription: FeedSubscription) : Observable<FeedChannelEntity>

}