package com.github.rssreader.features.feedsubscription.presentation.presenter

import com.github.rssreader.base.domain.UseCase
import com.github.rssreader.base.presentation.presenter.ErrorMessageHandler
import com.github.rssreader.base.presentation.presenter.Presenter
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionView
import io.reactivex.observers.DisposableObserver


class FeedSubscriptionPresenter(
        private val subscriptionUseCase: UseCase<FeedChannel, FeedSubscription>,
        private val errorMessageHandler: ErrorMessageHandler
) : Presenter<FeedSubscriptionView> {

    private lateinit var view: FeedSubscriptionView

    override fun attachTo(view: FeedSubscriptionView) {
        this.view = view
    }

    override fun destroy() = subscriptionUseCase.dispose()

    fun onFeedSubscriptionUrlConfirmed(url: String) {
        view.showLoading()
        subscriptionUseCase.execute(FeedSubscriptionObserver(), FeedSubscription(url))
    }

    // It is a good thing to use a DisposableCompletableObserver instead of DisposableObserver
    // because onNext method is not necessary in this case
    //
    // TODO: Inherit DisposableCompletableObserver instead of DisposableObserver
    inner class FeedSubscriptionObserver : DisposableObserver<FeedChannel>() {
        override fun onError(e: Throwable) {
            view.hideLoading()
            view.showErrorMessage(errorMessageHandler.handle(e.message))
        }

        override fun onNext(feedChannel: FeedChannel) {

        }

        override fun onComplete() {
            view.hideLoading()
        }
    }
}