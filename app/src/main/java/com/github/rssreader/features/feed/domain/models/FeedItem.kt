package com.github.rssreader.features.feed.domain.models


data class FeedItem(
        val title: String = "",
        val link: String = "",
        val description: String = "",
        val authorEmail: String = "",
        val categories: List<String> = ArrayList(),
        val pubDate: String = ""
)