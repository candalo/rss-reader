package com.github.rssreader.features.feedsubscription.presentation.view

import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import com.github.rssreader.R
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment


@RunWith(RobolectricTestRunner::class)
class FeedSubscriptionFragmentTest {

    private lateinit var fragment: FeedSubscriptionFragment
    private var feedUrlTextInputLayout: TextInputLayout? = null
    private var feedUrlProgressBar: ProgressBar? = null

    @Before
    fun init() {
        fragment = FeedSubscriptionFragment()
        startFragment(fragment)

        feedUrlTextInputLayout = fragment.view?.findViewById(R.id.text_input_layout_feed_url)
        feedUrlProgressBar = fragment.view?.findViewById(R.id.pb_feed_url)
    }

    @Test
    fun `Presenter is setup correctly`() {
        assertNotNull(fragment.presenter)
    }

    @Test
    fun `Feed subscription url confirmed`() {
        confirmFeedUrl()

        assertEquals(View.VISIBLE, feedUrlProgressBar?.visibility)
        assertEquals(View.INVISIBLE, feedUrlTextInputLayout?.visibility)
    }

    @Test
    fun `Success on feed subscription`() {
        confirmFeedUrl()
        fragment.presenter.FeedSubscriptionObserver().onComplete()

        assertEquals(View.INVISIBLE, feedUrlProgressBar?.visibility)
        assertEquals(View.VISIBLE, feedUrlTextInputLayout?.visibility)
    }

    @Test
    fun `Error on feed subscription`() {
        confirmFeedUrl()
        fragment.presenter.FeedSubscriptionObserver().onError(InvalidFeedSubscriptionException())

        assertEquals(View.INVISIBLE, feedUrlProgressBar?.visibility)
        assertEquals(View.VISIBLE, feedUrlTextInputLayout?.visibility)
    }

    private fun confirmFeedUrl() {
        feedUrlTextInputLayout?.editText?.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
    }
}
