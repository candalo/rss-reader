package com.github.rssreader.features.feed.presentation.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.rssreader.R
import com.github.rssreader.base.data.di.DaggerBaseComponent
import com.github.rssreader.features.feed.data.di.DaggerFeedContentListComponent
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedItem
import com.github.rssreader.features.feed.presentation.presenter.FeedContentListPresenter
import com.github.rssreader.features.feed.presentation.view.adapter.FeedContentListAdapter
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.network.DaggerNetworkComponent
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_feed_content_list.rv_feed_content_list as feedContentListRecyclerView
import kotlinx.android.synthetic.main.fragment_feed_content_list.pb_feed_content_list as feedContentListProgressBar


class FeedContentListFragment : Fragment(), FeedContentListView {

    @Inject lateinit var presenter: FeedContentListPresenter
    private lateinit var subscriptionUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscriptionUrl = getSubscriptionUrl()
    }

    private fun getSubscriptionUrl(): String = arguments?.getString(FeedSubscription::javaClass.name) ?: ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_content_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        setupPresenter()
    }

    private fun injectDependencies() {
        val baseComponent = DaggerBaseComponent.create()
        val networkComponent = DaggerNetworkComponent.create()

        DaggerFeedContentListComponent
                .builder()
                .baseComponent(baseComponent)
                .networkComponent(networkComponent)
                .build()
                .inject(this)
    }

    private fun setupPresenter() {
        presenter.attachTo(this)
        presenter.getFeed(subscriptionUrl)
    }

    override fun showLoading() {
        feedContentListProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        feedContentListProgressBar.visibility = View.INVISIBLE
    }

    override fun showErrorMessage(message: String) {
        // TODO: Implement
    }

    override fun onFeedLoaded(feed: Feed) {
        setupRecyclerView(feed.channel.items)
    }

    private fun setupRecyclerView(feedItems: List<FeedItem>) {
        feedContentListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = FeedContentListAdapter(feedItems)
        }
    }
}