package com.github.rssreader.features.feed.domain.usecases

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feed.domain.repository.FeedRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class GetFeedTest {

    private lateinit var useCase: GetFeed
    private var testObserver = TestObserver<Feed>()
    private var testScheduler = TestScheduler()

    @Mock
    private lateinit var subscriberThread: Thread
    @Mock
    private lateinit var observerThread: Thread
    @Mock
    private lateinit var feedRepository: FeedRepository

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { subscriberThread.get() }.willReturn(testScheduler)
        given { observerThread.get() }.willReturn(testScheduler)
    }

    @Test
    fun `Behavior on success`() {
        given { feedRepository.find("") }.willReturn(Observable.just(Feed(FeedChannel("", "", ""))))

        useCase = GetFeed(subscriberThread, observerThread, feedRepository)
        useCase.execute(testObserver, "")

        verify(feedRepository, times(1)).find("")

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun `Behavior on error`() {
        val exception = Exception()
        given { feedRepository.find("") }.willReturn(Observable.error(exception))

        useCase = GetFeed(subscriberThread, observerThread, feedRepository)
        useCase.execute(testObserver, "")

        verify(feedRepository, times(1)).find("")

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertError(exception)
        testObserver.assertNotComplete()
        testObserver.assertNoValues()
    }

}