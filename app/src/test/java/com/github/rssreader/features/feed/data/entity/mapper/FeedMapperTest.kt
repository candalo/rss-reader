package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedChannel
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


class FeedMapperTest {

    private val entityTestObserver = TestObserver<FeedEntity>()
    private val modelTestObserver = TestObserver<Feed>()
    private val testScheduler = TestScheduler()
    private val feedChannel = FeedChannel("", "", "")
    private val feedChannelEntity = FeedChannelEntity()

    @Mock
    private lateinit var thread: Thread
    @Mock
    private lateinit var feedChannelMapper: Mapper<FeedChannel, FeedChannelEntity>

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { thread.get() }.willReturn(testScheduler)
        given { feedChannelMapper.transformToEntity(any<FeedChannel>()) }.willReturn(Observable.just(feedChannelEntity))
        given { feedChannelMapper.transformToDomainModel(any<FeedChannelEntity>())}.willReturn(Observable.just(feedChannel))
    }

    @Test
    fun `Transform to Entity`() {
        val feed = Feed(feedChannel)

        val feedMapper = FeedMapper(feedChannelMapper)
        val feedEntityObservable = feedMapper.transformToEntity(feed)

        feedEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(entityTestObserver)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        entityTestObserver.assertValueCount(1)
        entityTestObserver.assertValue(FeedEntity())
        entityTestObserver.assertComplete()
        entityTestObserver.assertNoErrors()
    }

    @Test
    fun `Transform to Domain Model`() {
        val feedEntity = FeedEntity()

        val feedMapper = FeedMapper(feedChannelMapper)
        val feedObservable = feedMapper.transformToDomainModel(feedEntity)

        feedObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(modelTestObserver)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        modelTestObserver.assertValueCount(1)
        modelTestObserver.assertValue(Feed(feedChannel))
        modelTestObserver.assertComplete()
        modelTestObserver.assertNoErrors()
    }
}