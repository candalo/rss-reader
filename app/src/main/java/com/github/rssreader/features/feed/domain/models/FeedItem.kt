package com.github.rssreader.features.feed.domain.models

import java.text.SimpleDateFormat
import java.util.*


data class FeedItem(
        val title: String = "",
        val link: String = "",
        val description: String = "",
        val authorEmail: String = "",
        val category: String = "",
        val commentsLink: String = "",
        val pubDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
)