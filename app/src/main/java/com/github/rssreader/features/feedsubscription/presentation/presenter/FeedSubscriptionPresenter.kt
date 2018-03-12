package com.github.rssreader.features.feedsubscription.presentation.presenter

import com.github.rssreader.base.domain.CompletableUseCase
import com.github.rssreader.base.presentation.view.ErrorMessageHandler
import com.github.rssreader.base.presentation.presenter.Presenter
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionView
import io.reactivex.observers.DisposableCompletableObserver
import javax.inject.Inject


class FeedSubscriptionPresenter @Inject constructor(
        private val subscriptionUseCase: CompletableUseCase<@JvmSuppressWildcards FeedSubscription>,
        private val errorMessageHandler: ErrorMessageHandler
) : Presenter<FeedSubscriptionView> {

    private lateinit var view: FeedSubscriptionView

    override fun attachTo(view: FeedSubscriptionView) {
        this.view = view
        init()
    }

    override fun destroy() = subscriptionUseCase.dispose()

    private fun init() {
        view.setupToolbar()
    }

    fun onFeedSubscriptionUrlConfirmed(url: String) {
        view.showLoading()
        subscriptionUseCase.execute(FeedSubscriptionObserver(), FeedSubscription(url))
    }

    inner class FeedSubscriptionObserver : DisposableCompletableObserver() {
        override fun onError(e: Throwable) {
            view.hideLoading()
            view.showErrorMessage(errorMessageHandler.handle(e))
        }

        override fun onComplete() {
            view.hideLoading()
            view.showFeedSubscribedMessage()
        }
    }
}
