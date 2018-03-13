package com.github.rssreader.features.feed.presentation.view

import com.github.rssreader.base.presentation.view.LoadDataInterface
import com.github.rssreader.features.feed.domain.models.Feed


interface FeedContentListView : LoadDataInterface {

    fun onFeedLoaded(feed: Feed)

}