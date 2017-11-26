package com.github.rssreader.features.feedsubscription.presentation.view

import android.content.Context
import com.github.rssreader.R
import com.github.rssreader.base.presentation.view.ErrorMessageHandler
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import javax.inject.Inject


class FeedSubscriptionErrorMessageHandler @Inject constructor(private val context: Context) : ErrorMessageHandler {

    override fun handle(exception: Throwable): String {
        if (isInvalidFeedSubscription(exception)) {
            return context.getString(R.string.feed_subscription_invalid)
        }
        //TODO: Network error message
        return context.getString(R.string.generic_error)
    }

    private fun isInvalidFeedSubscription(exception: Throwable) =
            exception is InvalidFeedSubscriptionException

}
