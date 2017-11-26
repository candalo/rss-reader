package com.github.rssreader.features.feedsubscription.presentation.view

import android.content.Context
import com.github.rssreader.R
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class FeedSubscriptionErrorMessageHandlerTest {

    private lateinit var context: Context
    private lateinit var feedSubscriptionErrorMessageHandler: FeedSubscriptionErrorMessageHandler

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        context = RuntimeEnvironment.application
        feedSubscriptionErrorMessageHandler = FeedSubscriptionErrorMessageHandler(context)
    }

    @Test
    fun `Handle method result when receive an InvalidFeedSubscriptionException`() {
        val expected = feedSubscriptionErrorMessageHandler.handle(InvalidFeedSubscriptionException())
        assertEquals(context.getString(R.string.feed_subscription_invalid), expected)
    }

    @Test
    fun `Handle method result when receive a non InvalidFeedSubscriptionException`() {
        val expected = feedSubscriptionErrorMessageHandler.handle(Exception())
        assertEquals(context.getString(R.string.generic_error), expected)
    }
}
