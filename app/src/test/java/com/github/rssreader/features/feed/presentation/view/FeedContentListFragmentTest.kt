package com.github.rssreader.features.feed.presentation.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.github.rssreader.R
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feed.domain.models.FeedItem
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
class FeedContentListFragmentTest {

    private lateinit var fragment: FeedContentListFragment
    private var feedContentListRecyclerView: RecyclerView? = null
    private var feedContentListProgressBar: ProgressBar? = null

    @Before
    fun init() {
        fragment = FeedContentListFragment()
        SupportFragmentTestUtil.startFragment(fragment)

        feedContentListRecyclerView = fragment.view?.findViewById(R.id.rv_feed_content_list)
        feedContentListProgressBar = fragment.view?.findViewById(R.id.pb_feed_content_list)
    }

    @Test
    fun `Presenter is setup correctly`() {
        Assert.assertNotNull(fragment.presenter)
    }

    @Test
    fun `Success on get feed`() {
        Assert.assertEquals(View.VISIBLE, feedContentListProgressBar?.visibility)

        val items = arrayListOf<FeedItem>(FeedItem(), FeedItem(), FeedItem())
        val feed = Feed(FeedChannel("", "", "", items = items))
        fragment.presenter.GetFeedObserver().onNext(feed)
        fragment.presenter.GetFeedObserver().onComplete()

        Assert.assertTrue(feedContentListRecyclerView?.hasFixedSize()!!)
        Assert.assertEquals(3, feedContentListRecyclerView?.adapter?.itemCount)
        Assert.assertEquals(View.INVISIBLE, feedContentListProgressBar?.visibility)
    }

    @Test
    fun `Error on get feed`() {
        Assert.assertEquals(View.VISIBLE, feedContentListProgressBar?.visibility)

        fragment.presenter.GetFeedObserver().onError(Exception())

        Assert.assertEquals(View.INVISIBLE, feedContentListProgressBar?.visibility)
    }
}