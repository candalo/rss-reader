package com.github.rssreader.features.feedsubscription.presentation.presenter

import com.github.rssreader.base.domain.UseCase
import com.github.rssreader.base.presentation.view.ErrorMessageHandler
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionView
import com.nhaarman.mockito_kotlin.*
import io.reactivex.observers.DisposableObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FeedSubscriptionPresenterTest {

    private lateinit var feedSubscriptionPresenter: FeedSubscriptionPresenter
    private lateinit var feedSubscriptionObserver: FeedSubscriptionPresenter.FeedSubscriptionObserver

    @Mock private lateinit var mockSubscriptionUseCase: UseCase<Void, FeedSubscription>
    @Mock private lateinit var mockErrorMessageHandler: ErrorMessageHandler
    @Mock private lateinit var mockView: FeedSubscriptionView

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        feedSubscriptionPresenter = FeedSubscriptionPresenter(mockSubscriptionUseCase, mockErrorMessageHandler)
        feedSubscriptionPresenter.attachTo(mockView)

        feedSubscriptionObserver = feedSubscriptionPresenter.FeedSubscriptionObserver()
    }

    @Test
    fun `init_successfully`() {
        verify(mockView, times(1)).setupToolbar()
    }

    @Test
    fun `destroy_successfully`() {
        feedSubscriptionPresenter.destroy()

        verify(mockSubscriptionUseCase, times(1)).dispose()
    }

    @Test
    fun `onFeedSubscriptionUrlConfirmed_successfully`() {
        val url = "url"

        feedSubscriptionPresenter.onFeedSubscriptionUrlConfirmed(url)

        inOrder(mockView, mockSubscriptionUseCase) {
            verify(mockView, times(1)).showLoading()
            verify(mockSubscriptionUseCase, times(1)).execute(any<DisposableObserver<Void>>(), any<FeedSubscription>())
        }
    }

    @Test
    fun `feedSubscriptionObserver_successfully_willHideLoading`() {
        feedSubscriptionObserver.onComplete()

        inOrder(mockView) {
            verify(mockView, times(1)).hideLoading()
            verify(mockView, times(1)).showFeedSubscribedMessage()
        }
    }

    @Test
    fun `feedSubscriptionObserver_error_willHideLoadingAndShowErrorMessage`() {
        val errorMessage = "Invalid url"
        val exception = Exception(errorMessage)

        given { mockErrorMessageHandler.handle(exception) }.willReturn(errorMessage)

        feedSubscriptionObserver.onError(exception)

        inOrder(mockView) {
            verify(mockView, times(1)).hideLoading()
            verify(mockView, times(1)).showErrorMessage(errorMessage)
        }
    }
}
