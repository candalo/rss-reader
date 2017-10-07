package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedImageEntity
import com.github.rssreader.features.feed.domain.models.FeedImage
import com.nhaarman.mockito_kotlin.given
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class FeedImageMapperTest {

    private val feedImageMapper = FeedImageMapper()
    private val firstTestObserver = TestObserver<FeedImageEntity>()
    private val secondTestObserver = TestObserver<FeedImage>()
    private val testScheduler = TestScheduler()

    @Mock private lateinit var thread: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
    }

    @Test
    fun transformToEntity_completeDomainModel_willReturnEntityObservable() {
        val feedImage = FeedImage("url")

        val feedImageEntityObservable = feedImageMapper.transformToEntity(feedImage)

        feedImageEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedImageEntity = FeedImageEntity("url")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedImageEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()
    }

    @Test
    fun transformToEntity_onlyRequiredDomainModel_willReturnEntityObservable() {
        val feedImage = FeedImage()

        val feedImageEntityObservable = feedImageMapper.transformToEntity(feedImage)

        feedImageEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedImageEntity = FeedImageEntity()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedImageEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModel_completeEntity_willReturnDomainModelObservable() {
        val feedImageEntity = FeedImageEntity("url")

        val feedImageObservable = feedImageMapper.transformToDomainModel(feedImageEntity)

        feedImageObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feedImage = FeedImage("url")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feedImage)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModel_onlyRequiredEntity_willReturnDomainModelObservable() {
        val feedImageEntity = FeedImageEntity()

        val feedImageObservable = feedImageMapper.transformToDomainModel(feedImageEntity)

        feedImageObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feedImage = FeedImage()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feedImage)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()
    }
}