package com.github.rssreader.features.feedsubscription.presentation.view

import com.github.rssreader.base.presentation.view.LoadDataInterface
import com.github.rssreader.base.presentation.view.ToolbarView


interface FeedSubscriptionView : LoadDataInterface, ToolbarView {

    fun showFeedSubscribedMessage()

}
