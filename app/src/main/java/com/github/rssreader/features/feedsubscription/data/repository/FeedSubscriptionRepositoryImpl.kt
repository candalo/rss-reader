package com.github.rssreader.features.feedsubscription.data.repository

import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.CloudFeedSubscriptionDataSource
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import io.reactivex.Observable


class FeedSubscriptionRepositoryImpl(private val cloudFeedSubscriptionDataSource: CloudFeedSubscriptionDataSource) : FeedSubscriptionRepository {

    override fun create(feedSubscription: FeedSubscription): Observable<Void> =
            cloudFeedSubscriptionDataSource.create(feedSubscription)

}
