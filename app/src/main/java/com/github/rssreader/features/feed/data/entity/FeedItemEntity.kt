package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.text.SimpleDateFormat
import java.util.*

@Root(name = "item")
data class FeedItemEntity(
        @Element
        val title: String = "",
        @Element
        val link: String = "",
        @Element
        val description: String = "",
        @Element(name = "author")
        val authorEmail: String = "",
        @Element
        val category: String = "",
        @Element(name = "comments")
        val commentsLink: String = "",
        @Element
        val pubDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
)