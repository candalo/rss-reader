package com.github.rssreader.features.feedsubscription.presentation.view

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import com.github.rssreader.R
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FeedSubscriptionActivityTest {

    private lateinit var activity: FeedSubscriptionActivity
    private lateinit var feedUrlTextInputLayout: TextInputLayout
    private lateinit var feedUrlProgressBar: ProgressBar
    private lateinit var toolbar: Toolbar

    @Before
    fun init() {
        activity = Robolectric.setupActivity(FeedSubscriptionActivity::class.java)

        feedUrlTextInputLayout = activity.findViewById(R.id.text_input_layout_feed_url)
        feedUrlProgressBar = activity.findViewById(R.id.pb_feed_url)
        toolbar = activity.findViewById(R.id.toolbar)
    }

    @Test
    fun `Presenter is setup correctly`() {
        assertNotNull(activity.presenter)
    }

    @Test
    fun `Toolbar is setup correctly`() {
        assertEquals(activity.getString(R.string.feed_subscription_toolbar_title), toolbar.title)
    }

    @Test
    fun `Feed subscription url confirmed`() {
        confirmFeedUrl()

        assertEquals(View.VISIBLE, feedUrlProgressBar.visibility)
        assertEquals(View.INVISIBLE, feedUrlTextInputLayout.visibility)
    }

    @Test
    fun `Success on feed subscription`() {
        confirmFeedUrl()
        activity.presenter.FeedSubscriptionObserver().onComplete()

        assertEquals(View.INVISIBLE, feedUrlProgressBar.visibility)
        assertEquals(View.VISIBLE, feedUrlTextInputLayout.visibility)
    }

    @Test
    fun `Error on feed subscription`() {
        confirmFeedUrl()
        activity.presenter.FeedSubscriptionObserver().onError(InvalidFeedSubscriptionException())

        assertEquals(View.INVISIBLE, feedUrlProgressBar.visibility)
        assertEquals(View.VISIBLE, feedUrlTextInputLayout.visibility)
    }

    private fun confirmFeedUrl() {
        feedUrlTextInputLayout.editText?.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
    }
}
