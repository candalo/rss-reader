package com.github.rssreader.features.feedsubscription.domain.usecases

import com.github.rssreader.base.domain.BaseUseCase
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import io.reactivex.Observable
import javax.inject.Inject


class Subscribe @Inject constructor(subscriberThread: Thread,
                                    observerThread: Thread,
                                    private val feedSubscriptionRepository: FeedSubscriptionRepository
) : BaseUseCase<Void, FeedSubscription>(subscriberThread, observerThread) {

    override fun getObservable(params: FeedSubscription): Observable<Void> =
            feedSubscriptionRepository.create(params)
}
