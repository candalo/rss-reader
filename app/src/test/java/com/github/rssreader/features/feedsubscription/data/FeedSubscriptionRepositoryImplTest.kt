package com.github.rssreader.features.feedsubscription.data

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feedsubscription.data.repository.FeedSubscriptionRepositoryImpl
import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.CloudFeedSubscriptionDataSource
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class FeedSubscriptionRepositoryImplTest {

    companion object {
        const val VALID_FEED_URL = "https://valid-feed-url.com"
        const val INVALID_FEED_URL = "https://invalid-url-feed.com"
    }

    private val testObserver = TestObserver<Void>()
    private val testScheduler = TestScheduler()
    private lateinit var repository: FeedSubscriptionRepositoryImpl

    @Mock private lateinit var cloudFeedSubscriptionDataSource: CloudFeedSubscriptionDataSource
    @Mock private lateinit var thread: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)

        repository = FeedSubscriptionRepositoryImpl(cloudFeedSubscriptionDataSource)
    }

    @Test
    fun create_validFeedSubscription_willReturnEmptyObservable() {
        given { cloudFeedSubscriptionDataSource.create(any<FeedSubscription>()) }.willReturn(Observable.empty())

        val feedSubscription = FeedSubscription(VALID_FEED_URL)

        val observable = repository.create(feedSubscription)
        observable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun create_invalidFeedSubscription_willReturnError() {
        val exception = InvalidFeedSubscriptionException()
        given { cloudFeedSubscriptionDataSource.create(any<FeedSubscription>()) }.willReturn(Observable.error(exception))

        val feedSubscription = FeedSubscription(INVALID_FEED_URL)

        val observable = repository.create(feedSubscription)
        observable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertError(exception)
        testObserver.assertNotComplete()
        testObserver.assertNoValues()
    }
}
