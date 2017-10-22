package com.github.rssreader.features.feedsubscription.data.repository.datasource

import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.CloudFeedSubscriptionDataStore
import io.reactivex.Observable


class FeedSubscriptionDataStoreFactoryImpl(private val cloudFeedSubscriptionDataStore: CloudFeedSubscriptionDataStore) : FeedSubscriptionDataStoreFactory {

    override fun create(): Observable<FeedSubscriptionDataStore> =
            Observable.just(cloudFeedSubscriptionDataStore)

}