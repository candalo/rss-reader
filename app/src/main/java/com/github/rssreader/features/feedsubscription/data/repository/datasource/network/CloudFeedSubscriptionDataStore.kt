package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feedsubscription.data.repository.datasource.FeedSubscriptionDataStore
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import io.reactivex.Observable


class CloudFeedSubscriptionDataStore(private val restApi: FeedSubscriptionRestApi) : FeedSubscriptionDataStore {

    override fun create(feedSubscription: FeedSubscription): Observable<FeedChannelEntity> =
            restApi.subscribe(feedSubscription.url).map { feedEntity -> feedEntity.channel }

}