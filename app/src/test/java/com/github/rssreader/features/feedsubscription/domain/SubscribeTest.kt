package com.github.rssreader.features.feedsubscription.domain

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import com.github.rssreader.features.feedsubscription.domain.usecases.Subscribe
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class SubscribeTest {

    companion object {
        const val VALID_FEED_URL = "https://valid-feed-url.com"
        const val INVALID_FEED_URL = "https://invalid-url-feed.com"
    }

    private lateinit var subscribeUseCase: Subscribe
    private var feedSubscriptionTestObserver = TestObserver<Void>()
    private var testScheduler = TestScheduler()

    @Mock private lateinit var subscriberThread: Thread
    @Mock private lateinit var observerThread: Thread
    @Mock private lateinit var feedSubscriptionRepository: FeedSubscriptionRepository
    @Mock private lateinit var feed: FeedChannel

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { subscriberThread.get() }.willReturn(testScheduler)
        given { observerThread.get() }.willReturn(testScheduler)
    }

    @Test
    fun execute_validFeedUrl_willReturnEmptyCompletable() {
        val completable = Completable.complete()
        given { feedSubscriptionRepository.create(FeedSubscription(VALID_FEED_URL)) }.willReturn(completable)

        subscribeUseCase = Subscribe(subscriberThread, observerThread, feedSubscriptionRepository)
        subscribeUseCase.execute(feedSubscriptionTestObserver, FeedSubscription(VALID_FEED_URL))

        verify(feedSubscriptionRepository, times(1)).create(FeedSubscription(VALID_FEED_URL))

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        feedSubscriptionTestObserver.assertComplete()
        feedSubscriptionTestObserver.assertNoErrors()
    }

    @Test
    fun execute_invalidFeedUrl_willReturnError() {
        val exception = InvalidFeedSubscriptionException()
        given { feedSubscriptionRepository.create(FeedSubscription(INVALID_FEED_URL)) }
                .willReturn(Completable.error(exception))

        subscribeUseCase = Subscribe(subscriberThread, observerThread, feedSubscriptionRepository)
        subscribeUseCase.execute(feedSubscriptionTestObserver, FeedSubscription(INVALID_FEED_URL))

        verify(feedSubscriptionRepository, times(1)).create(FeedSubscription(INVALID_FEED_URL))

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        feedSubscriptionTestObserver.assertError(exception)
        feedSubscriptionTestObserver.assertNotComplete()
        feedSubscriptionTestObserver.assertNoValues()
    }
}
