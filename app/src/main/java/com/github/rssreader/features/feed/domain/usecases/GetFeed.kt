package com.github.rssreader.features.feed.domain.usecases

import com.github.rssreader.base.domain.BaseUseCase
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.repository.FeedRepository
import io.reactivex.Observable
import javax.inject.Inject


class GetFeed @Inject constructor(subscriberThread: Thread,
                                  observerThread: Thread,
                                  private val feedRepository: FeedRepository
) : BaseUseCase<Feed, String>(subscriberThread, observerThread) {

    override fun getObservable(params: String): Observable<Feed> = feedRepository.find(params)

}