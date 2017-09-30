package com.github.rssreader.features.feedsubscription.data.repository.datasource

import io.reactivex.Observable


interface FeedSubscriptionDataStoreFactory {

    fun create() : Observable<FeedSubscriptionDataStore>

}