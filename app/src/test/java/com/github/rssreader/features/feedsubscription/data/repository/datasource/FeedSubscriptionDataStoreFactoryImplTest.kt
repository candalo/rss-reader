package com.github.rssreader.features.feedsubscription.data.repository.datasource

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.CloudFeedSubscriptionDataStore
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class FeedSubscriptionDataStoreFactoryImplTest {

    private val testObserver = TestObserver<FeedSubscriptionDataStore>()
    private val testScheduler = TestScheduler()

    @Mock private lateinit var thread: Thread
    @Mock private lateinit var cloudFeedSubscriptionDataStore: CloudFeedSubscriptionDataStore

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
        given { cloudFeedSubscriptionDataStore.create(any<FeedSubscription>()) }.willReturn(Observable.just(FeedChannelEntity()))
    }

    @Test
    fun `create_willReturnFeedSubscriptionDataStoreObservable`() {
        val feedSubscriptionDataStoreFactory = FeedSubscriptionDataStoreFactoryImpl(cloudFeedSubscriptionDataStore)
        val feedSubscriptionDataStoreObservable = feedSubscriptionDataStoreFactory.create()

        feedSubscriptionDataStoreObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        assertThat(testObserver.values()[0], instanceOf(FeedSubscriptionDataStore::class.java))
    }
}