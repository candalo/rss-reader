package com.github.rssreader.features.feedsubscription.domain.usecases

import com.github.rssreader.base.domain.BaseUseCase
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import io.reactivex.Observable


class Subscribe(subscriberThread: Thread,
                observerThread: Thread,
                private val feedSubscriptionRepository: FeedSubscriptionRepository
) : BaseUseCase<FeedChannel, FeedSubscription>(subscriberThread, observerThread) {

    override fun getObservable(params: FeedSubscription): Observable<FeedChannel> =
            feedSubscriptionRepository.create(params)
            // TODO: Save the feedChannel in some way
}