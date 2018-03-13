package com.github.rssreader.features.feed.data.repository

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.data.repository.datasource.CloudFeedDataSource
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

class FeedRepositoryImplTest {

    private val testObserver = TestObserver<Feed>()
    private val testScheduler = TestScheduler()
    private lateinit var repository: FeedRepositoryImpl

    @Mock
    private lateinit var cloudFeedDataSource: CloudFeedDataSource
    @Mock
    private lateinit var mapper: Mapper<Feed, FeedEntity>
    @Mock
    private lateinit var thread: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { thread.get() }.willReturn(testScheduler)
        repository = FeedRepositoryImpl(cloudFeedDataSource, mapper)
    }

    @Test
    fun `Success on find`() {
        given { cloudFeedDataSource.find(any<String>()) }.willReturn(Observable.just(FeedEntity()))
        given { mapper.transformToDomainModel(any<FeedEntity>()) }.willReturn(Observable.just(Feed(FeedChannel("", "", ""))))

        val observable = repository.find("")
        observable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun `Error on find`() {
        val exception = Exception()
        given { cloudFeedDataSource.find(any<String>()) }.willReturn(Observable.error(exception))
        given { mapper.transformToDomainModel(any<FeedEntity>()) }.willReturn(Observable.just(Feed(FeedChannel("", "", ""))))

        val observable = repository.find("")
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