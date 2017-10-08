package com.github.rssreader.features.feedsubscription.data

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feedsubscription.data.repository.FeedSubscriptionRepositoryImpl
import com.github.rssreader.features.feedsubscription.data.repository.datasource.FeedSubscriptionDataStore
import com.github.rssreader.features.feedsubscription.data.repository.datasource.FeedSubscriptionDataStoreFactory
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
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

    private val testObserver = TestObserver<Feed>()
    private val testScheduler = TestScheduler()
    private lateinit var repository : FeedSubscriptionRepository

    @Mock private lateinit var dataStoreFactory: FeedSubscriptionDataStoreFactory
    @Mock private lateinit var dataStore: FeedSubscriptionDataStore
    @Mock private lateinit var dataMapper: Mapper<Feed, FeedEntity>
    @Mock private lateinit var thread: Thread
    @Mock private lateinit var feed: Feed
    @Mock private lateinit var feedEntity: FeedEntity

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
        given { dataMapper.transformToDomainModel(feedEntity)}.willReturn(Observable.just(feed))

        repository = FeedSubscriptionRepositoryImpl(dataStoreFactory, dataMapper)
    }

    @Test
    fun create_validFeedSubscription_willReturnFeed() {
        given { dataStore.create(any<FeedSubscription>())}.willReturn(Observable.just(feedEntity))
        given { dataStoreFactory.create()}.willReturn(Observable.just(dataStore))

        val feedSubscription = FeedSubscription(VALID_FEED_URL)

        val observable = repository.create(feedSubscription)
        observable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertValue(feed)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun create_invalidFeedSubscription_willReturnError() {
        val exception = InvalidFeedSubscriptionException()
        given { dataStore.create(any<FeedSubscription>())}.willReturn(Observable.error(exception))
        given { dataStoreFactory.create()}.willReturn(Observable.just(dataStore))

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