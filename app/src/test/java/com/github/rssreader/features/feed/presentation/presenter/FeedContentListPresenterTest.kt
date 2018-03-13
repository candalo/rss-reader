package com.github.rssreader.features.feed.presentation.presenter

import com.github.rssreader.base.domain.UseCase
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feed.presentation.view.FeedContentListView
import com.nhaarman.mockito_kotlin.*
import io.reactivex.observers.DisposableObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class FeedContentListPresenterTest {

    private lateinit var presenter: FeedContentListPresenter
    private lateinit var observer: FeedContentListPresenter.GetFeedObserver

    @Mock
    private lateinit var mockGetFeedUseCase: UseCase<Feed, String>
    @Mock
    private lateinit var mockView: FeedContentListView

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        presenter = FeedContentListPresenter(mockGetFeedUseCase)
        presenter.attachTo(mockView)

        observer = presenter.GetFeedObserver()
    }

    @Test
    fun `Destroy behavior`() {
        presenter.destroy()
        verify(mockGetFeedUseCase, times(1)).dispose()
    }

    @Test
    fun `GetFeed behavior`() {
        val url = "url"

        presenter.getFeed(url)

        inOrder(mockView, mockGetFeedUseCase) {
            verify(mockView, times(1)).showLoading()
            verify(mockGetFeedUseCase, times(1)).execute(any<DisposableObserver<Feed>>(), any<String>())
        }
    }

    @Test
    fun `Successfully call behavior`() {
        observer.onNext(Feed(FeedChannel("", "", "")))
        observer.onComplete()

        inOrder(mockView) {
            verify(mockView, times(1)).onFeedLoaded(any<Feed>())
            verify(mockView, times(1)).hideLoading()
        }
    }

    @Test
    fun `Error call behavior`() {
        val exception = Exception()

        observer.onError(exception)

        inOrder(mockView) {
            verify(mockView, times(1)).hideLoading()
        }
    }

}