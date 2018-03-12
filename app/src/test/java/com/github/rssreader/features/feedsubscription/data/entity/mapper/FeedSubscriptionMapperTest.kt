package com.github.rssreader.features.feedsubscription.data.entity.mapper

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.nhaarman.mockito_kotlin.given
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class FeedSubscriptionMapperTest {

    private val feedSubscriptionMapper = FeedSubscriptionMapper()
    private val firstTestObserver = TestObserver<FeedSubscriptionEntity>()
    private val secondTestObserver = TestObserver<FeedSubscription>()
    private val testScheduler = TestScheduler()

    @Mock
    private lateinit var thread: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
    }

    @Test
    fun transformToEntity_completeDomainModel_willReturnEntityObservable() {
        val feedSubscription = FeedSubscription("url")

        val feedSubscriptionObservable = feedSubscriptionMapper.transformToEntity(feedSubscription)

        feedSubscriptionObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedSubscriptionEntity = FeedSubscriptionEntity("url")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedSubscriptionEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModel_completeEntity_willReturnDomainModelObservable() {
        val feedSubscriptionEntity = FeedSubscriptionEntity("url")

        val feedSubscriptionObservable = feedSubscriptionMapper.transformToDomainModel(feedSubscriptionEntity)

        feedSubscriptionObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feedSubscription = FeedSubscription("url")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feedSubscription)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()
    }
}