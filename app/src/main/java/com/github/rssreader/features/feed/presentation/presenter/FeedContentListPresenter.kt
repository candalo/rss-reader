package com.github.rssreader.features.feed.presentation.presenter

import com.github.rssreader.base.domain.UseCase
import com.github.rssreader.base.presentation.presenter.Presenter
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.presentation.view.FeedContentListView
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class FeedContentListPresenter @Inject constructor(
        private val getFeedUseCase: UseCase<@JvmSuppressWildcards Feed, @JvmSuppressWildcards String>
) : Presenter<FeedContentListView> {

    private lateinit var view: FeedContentListView

    override fun attachTo(view: FeedContentListView) {
        this.view = view
    }

    override fun destroy() {
        getFeedUseCase.dispose()
    }

    fun getFeed(feedUrl: String) {
        view.showLoading()
        getFeedUseCase.execute(GetFeedObserver(), feedUrl)
    }

    inner class GetFeedObserver : DisposableObserver<Feed>() {
        override fun onComplete() {
            view.hideLoading()
        }

        override fun onNext(feed: Feed) {
            view.onFeedLoaded(feed)
        }

        override fun onError(e: Throwable) {
            view.hideLoading()
        }
    }
}