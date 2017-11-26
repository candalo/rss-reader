package com.github.rssreader.features.feedsubscription.domain.repository

import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import io.reactivex.Observable


interface FeedSubscriptionRepository {

    fun create(feedSubscription: FeedSubscription) : Observable<Void>

}
